import requests
from flask import Flask, request, jsonify, send_file
from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.common.by import By
import ssl
import time
from io import BytesIO
from webdriver_manager.chrome import ChromeDriverManager
from flask_cors import CORS

app = Flask(__name__)
CORS(app)

def crawl_instagram(hashtag):
    chrome_options = Options()
    chrome_options.add_argument("--headless")  # 헤드리스 모드 설정
    chrome_options.add_argument("--disable-gpu")
    chrome_options.add_argument("--window-size=1920x1080")
    chrome_options.add_argument("--no-sandbox")
    chrome_options.add_argument("--disable-dev-shm-usage")
    chrome_options.add_experimental_option("excludeSwitches", ["enable-logging"])

    # 매번 새로운 WebDriver 인스턴스 생성 -> 해시태그 변경에 따른 결과물 변경을 위해 사용
    service = Service(ChromeDriverManager().install())
    driver = webdriver.Chrome(service=service, options=chrome_options)

    url = f"https://www.instagram.com/explore/tags/{hashtag}/"
    driver.get(url)

    WebDriverWait(driver, 10).until(
        EC.presence_of_element_located((By.XPATH, '//article//a'))
    )

    # 상위 6개 게시물 링크
    posts = driver.find_elements(By.XPATH, '//article//a')[:6]
    results = []

    for post in posts:
        post_url = post.get_attribute('href')
        img_url = post.find_element(By.TAG_NAME, 'img').get_attribute('src')
        results.append({'post_url': post_url, 'img_url': img_url})

    driver.quit()
    return results

@app.route('/api/search', methods=['GET'])
def search():
    hashtag = request.args.get('hashtag')
    if not hashtag:
        return jsonify({"error": "Hashtag is required"}), 400

    try:
        posts = crawl_instagram(hashtag)
        return jsonify(posts)
    except Exception as e:
        return jsonify({"error": str(e)}), 500

@app.route('/api/image', methods=['GET'])
def get_image():
    url = request.args.get('url')
    response = requests.get(url)
    img = BytesIO(response.content)
    return send_file(img, mimetype='image/jpeg')

@app.route('/')
def home():
    return "Instagram Crawler API"

if __name__ == '__main__':
    app.run(debug=True)
