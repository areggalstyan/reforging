import json
from glob import glob
from os import chdir, system
from os.path import abspath
from time import sleep

from selenium.webdriver import Keys
from selenium.webdriver.common.by import By
from undetected_chromedriver import Chrome, ChromeOptions

login_xpath = '/html/body/div[2]/div/div[2]/div[2]/div[2]/div/div/div[2]/div/form/dl[3]/dd/input'
upload_resource_xpath = '/html/body/div[1]/div/div[2]/div[2]/div[2]/div/div/div[2]/div/form/fieldset[1]/dl[' \
                        '1]/dd/ul/li[1]/ul/li/span/span[2]/input[2]'
message_xpath = '/html/body/div[1]/div/div[2]/div[2]/div[2]/div/div/div[2]/div/form/fieldset[2]/dl[2]/dd/div/div/iframe'
description_xpath = '/html/body/div[1]/div/div[2]/div[2]/div[2]/div/div/div[2]/div/form/fieldset[3]/dl[' \
                    '1]/dd/div/div/iframe'
upload_images_xpath = '/html/body/div[1]/div/div[2]/div[2]/div[2]/div/div/div[2]/div/form/dl/dd/span/span[2]/input[2]'
title = f'REFORGING | {input("Update Title: ").upper()}'
message = ''
screenshots = []

with open('CREDENTIALS.txt', 'r') as file:
    spigotmc_credentials = {
        'login': file.readline(),
        'password': file.readline()
    }

    github_credentials = {
        'login': file.readline(),
        'password': file.readline()
    }

with open('../manifest.json', 'r') as file:
    version = json.load(file)['version'].split('-')[0]


def login_github():
    driver.get('https://github.com/Aregcraft/reforging/releases/new')
    sleep(3)
    driver.execute_script('''
    let login = document.querySelector("#login_field");
    if (login != null) {
        login.value = arguments[0];
        document.querySelector("#password").value = arguments[1];
        document.querySelector(".js-sign-in-button").click();
    }
    ''', github_credentials['login'], github_credentials['password'])


def create_release():
    login_github()
    sleep(3)
    driver.find_element(By.CSS_SELECTOR, '.js-release-tag > div > div:first-child').click()
    driver.find_element(By.CSS_SELECTOR, '[placeholder="Find or create a new tag"]').send_keys(f'v{version}')
    sleep(3)
    driver.execute_script('document.querySelector(".SelectMenu-item.text-bold").click()')
    driver.execute_script('document.querySelector("#release_name").value = `Reforging ${arguments[0]}`', version)
    driver.find_element(By.ID, 'releases-upload').send_keys(glob(abspath('../debug/spigot/plugins/*.jar'))[0])
    sleep(3)
    driver.execute_script('document.querySelector(".js-publish-release").click()')


def convert_to_jpg():
    global screenshots, message
    png = glob(abspath('../screenshots/*'))
    jpg = map(lambda x: x.replace('jpg', 'png').replace('/scripts', ''), glob(abspath('screenshots/*')))
    screenshots = set(png) - set(jpg)
    if len(screenshots) == 0:
        message = input('Message: ')
        return
    driver.get('https://png2jpg.com/')
    driver.find_element(By.ID, 'fileSelector').send_keys('\n'.join(screenshots))
    sleep(3 * len(screenshots))
    if driver.execute_script('''
    if ([...document.querySelectorAll(".file__state_visible .file__state-text")]
        .some(it => it.textContent === "ERROR")) {
        return true;
    }
    document.querySelectorAll(".file-button").forEach(it => it.click());
    '''):
        convert_to_jpg()


def login_spigotmc():
    driver.get('https://www.spigotmc.org/resources/reforging.105707/add-version')
    sleep(3)
    driver.execute_script('''
    let login = [...document.querySelectorAll("[name='login']")].at(-1);
    if (login != null) {
        login.value = arguments[0];
        [...document.querySelectorAll("[name='password']")].at(-1).value = arguments[1];
        document.evaluate(arguments[2], document, null, XPathResult.ANY_TYPE, null).iterateNext().click();
    }
    ''', spigotmc_credentials['login'], spigotmc_credentials['password'], login_xpath)


def upload_images():
    images = map(lambda x: x.replace('png', 'jpg').replace('screenshots', 'scripts/screenshots'), screenshots)
    driver.find_element(By.XPATH, upload_images_xpath).send_keys('\n'.join(images))
    sleep(2 * len(screenshots))
    driver.execute_script('document.querySelector(".AttachmentInsertAll[name=\'image\']").click()')


def post_resource_update():
    login_spigotmc()
    sleep(3)
    driver.find_element(By.XPATH, upload_resource_xpath).send_keys(glob(abspath('../debug/spigot/plugins/*.jar'))[0])
    driver.execute_script('document.querySelector("[name=\'version_string\']").value = arguments[0]', version)
    driver.execute_script('document.querySelector("[name=\'title\']").value = arguments[0]', title)
    if len(screenshots) == 0:
        driver.execute_script('document.evaluate(arguments[1], document, null, XPathResult.ANY_TYPE, '
                              'null).iterateNext().contentWindow.document.body.innerHTML = `<p>${arguments[0]}</p>`',
                              message, message_xpath)
    else:
        upload_images()
    sleep(3)
    driver.execute_script('document.querySelector("[value=\'Save Update\']").click()')


def edit_resource_description():
    driver.get('https://www.spigotmc.org/resources/reforging.105707/edit')
    sleep(3)
    driver.execute_script('''
    let body = document.evaluate(arguments[0], document, null, XPathResult.ANY_TYPE, null)
        .iterateNext().contentWindow.document.body;
    body.removeChild(body.lastElementChild);
    body.innerHTML += '<p><br></p>';
    ''', description_xpath)
    driver.switch_to.frame(driver.find_element(By.XPATH, description_xpath))
    driver.find_element(By.CSS_SELECTOR, 'body > p:last-child').send_keys(Keys.END)
    driver.switch_to.default_content()
    upload_images()
    sleep(3)
    driver.execute_script('document.querySelector("[value=\'Save\']").click()')


chdir('..')
system('./gradlew prepareRelease')
chdir('scripts')
options = ChromeOptions()
prefs = {
    'download.default_directory': abspath("screenshots"),
    'profile.default_content_setting_values.automatic_downloads': 1
}
options.add_experimental_option("prefs", prefs)
driver = Chrome(options=options)
create_release()
sleep(6)
convert_to_jpg()
post_resource_update()
sleep(6)
if len(screenshots) != 0:
    edit_resource_description()
sleep(6)
driver.close()
