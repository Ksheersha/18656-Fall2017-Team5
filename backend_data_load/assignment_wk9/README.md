# Week 9 - Data Collection

Imre Nagi <inagi@andrew.cmu.edu>

## Installation

* It will be easier if you are using `virtualenv` for Python 2.7. Please install it if you don't have one.
* To init the virtualenv, run `virtualenv -p python env`
* To run virtualenv, run `source env/bin/activate`
* Run `pip install -r requirements.txt` to install dependencies
* This app uses BING Search API and it requires BING SEARCH API KEY. Please follow instruction [here](https://azure.microsoft.com/en-us/try/cognitive-services/?api=bing-web-search-api)
* After you get the API KEY, please go to `crawler/crawler/spiders/techprogramcommitteecrawler.py` and put your api key in this variable:
```
subscriptionKey = ""
```
