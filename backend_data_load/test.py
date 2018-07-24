import urllib
import urllib2
import json

authorName='Jia Zhang'

authorName=urllib.quote(authorName, safe='')
url = 'http://localhost:9000/getAuthorDirNetwork/' + authorName
response = urllib2.urlopen(url)
res = response.read().strip()

coauthors = []
authors = json.loads(res)
for author in authors:
    coauthors.append(author['name'])

print coauthors    
