import re
import urllib2, sys
from bs4 import BeautifulSoup
from xml.sax.saxutils import quoteattr
#
# Fetch all club names and links into string-array resources
# for Android. Classical messy but functional scripting

html = urllib2.urlopen("http://www.rowan.edu/clubs/clublist/alphabetical.cfm").read()
soup = BeautifulSoup(html)
hostPath = "http://rowan.edu/clubs/clublist/"
clubRegex = re.compile('.*clubinfo.*') # matches anything that contains a clubinfo link
nameXML = '<string-array name="clubs"> \r\n\t'
linkXML = '<string-array name="clubsURLs"> \r\n\t'

try:
	contentDiv = soup.find('div', attrs={'id': 'bodyTextWrapper'})
	links = contentDiv.findAll('a')
	numOfClubs = len(links)
	progress = 0
	for link in links:
		print "status: %d/%d" % (progress, numOfClubs)
		progress = progress + 1
		actualURL = str(link.get('href'))
		if clubRegex.match(actualURL): # filter only club links
			# add club name and escape special characters for XML content
			nameXML = nameXML + '<item>' + quoteattr(link.get_text()) + '</item>\r\n\t'
			fullClubAddress = hostPath + actualURL
			### find actual website
			clubSoup = BeautifulSoup(urllib2.urlopen(fullClubAddress))
			clubWebsite = clubSoup.find('div', attrs={'id': 'bodyTextWrapper'}).find('a')
			if clubWebsite is None:
				linkXML = linkXML + '<item>unknown</item>' + '\r\n\t'
				#print "unknown"
			else:
				linkXML = linkXML + '<item>' + str(clubWebsite.get('href')) + '</item> \r\n\t'
				#print str(clubWebsite.get('href')) 

except IOError:
	print 'IO error'

nameXML = nameXML + '</string-array>'
linkXML = linkXML + '</string-array>'

print nameXML
print "---"
print "---"
print linkXML
