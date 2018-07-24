# import urllib
# import urllib2
#
# import httplib
# from urlparse import urlparse
#
# # subscriptionKey = "fd414009f492490fb1d9d319fffff0f0"
# # url = 'api.cognitive.microsoft.com'
# # values = {'q': 'Jia Zhang Scholar'}
# # headers = {'Ocp-Apim-Subscription-Key': subscriptionKey}
# #
# # httplib.HTTPConnection('www.cwi.nl')
# #
# # data = urllib.urlencode(values)
# # req = urllib2.Request(url, data, headers)
# # response = urllib2.urlopen(req)
# # the_page = response.read()
# # -*- coding: utf-8 -*-
#
# import httplib,  json
#
# # **********************************************
# # *** Update or verify the following values. ***
# # **********************************************
#
# # Replace the subscriptionKey string value with your valid subscription key.
# subscriptionKey = "fd414009f492490fb1d9d319fffff0f0"
#
# host = "api.cognitive.microsoft.com"
# path = "/bing/v7.0/search"
#
# term = "Microsoft Cognitive Services"
#
# def BingWebSearch(search):
#     "Performs a Bing Web search and returns the results."
#
#     headers = {'Ocp-Apim-Subscription-Key': subscriptionKey}
#     conn = httplib.HTTPSConnection(host)
#     values = {'q': 'Jia Zhang Carnegie Mellon University Google Scholar'}
#     query = urllib.urlencode(values)
#     conn.request("GET", path + "?" + query, headers=headers)
#     response = conn.getresponse()
#     headers = [k + ": " + v for (k, v) in response.getheaders()
#                    if k.startswith("BingAPIs-") or k.startswith("X-MSEdge-")]
#     return headers, response.read().decode("utf8")
#
# if len(subscriptionKey) == 32:
#
#     print('Searching the Web for: ', term)
#
#     headers, result = BingWebSearch(term)
#     print("\nRelevant HTTP Headers:\n")
#     print("\n".join(headers))
#     print("\nJSON Response:\n")
#     # print(result)
#     # print(json.dumps(json.loads(result)['webPages']['value'], indent=4))
#     print(json.dumps(json.loads(result), indent=4))
#
#     data = json.loads(result)['webPages']['value']
#
#     for obj in data:
#         # print obj
#         parsedUrl = urlparse(obj['url'])
#         if parsedUrl.netloc == 'scholar.google.com':
#             print obj
#
#     # data = json.dumps(, indent=4)
#     # print json.loads(result)['webPages']
#
# else:
#
#     print("Invalid Bing Search API subscription key!")
#     print("Please paste yours into the source code.")
