import requests
import random
import json
import time
import xml.etree.ElementTree 

def retrieveGeoInfo(level,num_answers,princ_country,player):
    if(princ_country==None): princ_country='NotInsertedCountry'
    lat, long, country, city='','','',''
    latRange =[-180,+180]
    lonRange =[-60,+70]
    while(country=='' or country==princ_country or princ_country in country):
        lat,long,country,city = chooseGoodCoordinates(latRange,lonRange,level,num_answers,player)
        #print("country: "+str(country))
        #print("princ_country: "+princ_country)
    return lat,long,country,city 

        
def retrieveSimilarAnswers(sourceCountry, sourceCity,false_coutry,false_city):
    answers=[]
    if(isinstance(sourceCountry, list)): i=[1,3]
    else: i=[0,1]
    while(i[0]<i[1]):
        #if( sourceCountry[i]!=country or (len(sourceCountry)<=3 )):
        if(i[1]==1): answers.append({'country':sourceCountry, 'city':sourceCity})
        else: answers.append({'country':sourceCountry[i[0]], 'city':sourceCity[i[0]]})
        i[0]+=1
    if(i[1]==3): answers.append({'country':false_coutry, 'city':false_city})
    else:
        j=0
        while(j<len(false_coutry)):
            answers.append({'country':false_coutry[j], 'city':false_city[j]})
            j+=1
    return answers

def chooseGoodCoordinates(latRange,lonRange,level,num_answers,player):
    lat,long = [],[]
    country,city =[],[]
    #goodCoord=False
    maxRows=3+level+3 #manage number of answers to retrieve
    maxRows=str(maxRows)
    count=0
    if(num_answers==1): marginLat=40; marginLong = 20
    else: marginLat=20; marginLong = 10
    #filter coordinates
    #while(not goodCoord):
    partialLat= random.uniform(latRange[0],latRange[1])
    partialLong = random.uniform(lonRange[0],lonRange[1])
    '''if( ( partialLat>-45 and partialLong<-40  ) #remove southern ocean
                    or  (partialLat< -82 and partialLong<+7)  #remove south ocean pacific
                    or  (partialLat>+50 and partialLat<+105 and partialLong>=-40 and partialLong<-7)  #remove indian ocean
                    or  (partialLat<-125 and  partialLong<54 and partialLong>+7)#remove north pacific ocean
                    or  (partialLat>+127 and partialLong>+0 and partialLong< +30)#remove pacific ocean
                    or  (partialLat>+150 and partialLong>+30 and partialLong< +60)#remove pacific ocean
                    or  (partialLat>-45 and partialLat<+14 and partialLong<-22 and partialLong>=-40)#remove south Atlantic ocean 
                    or  (partialLat>-35 and partialLat<+8 and partialLong>=-22 and partialLong<2)#remove south Atlantic ocean 
                    or  (partialLat>-50 and partialLat<-17 and partialLong>+0 and partialLong<+15 )#remove north Atlantic ocean
                    or  (partialLat>-60 and partialLat<-17 and partialLong>+8 and partialLong<+45 )#remove north Atlantic ocean 
                    or  (partialLat>-53 and partialLat<-9 and  partialLong>=+45 and partialLong<+60 )#remove north Atlantic ocean 
                    or  (partialLat>+75 and  partialLong>=+55)#remove part of russia
                      ): 
                goodCoord= False

            else:'''
    #        goodCoord = True
    #SEND REQUESTS UNTIL RETURN A RESPONSE
   
    while(len(country)<num_answers):#retrieve num. land = num_answers

        #choose range for fin lands
        if(count>0): marginLat*=2; marginLong*=2
        if(partialLat<= 180-marginLat and partialLat>=-180+marginLat):
            east = partialLat+marginLat
            west = partialLat-marginLat
        elif(partialLat< 180-marginLat and partialLat<-180+marginLat):
            east = -180+ marginLat*2
            west = -180
        elif(partialLat> 180-marginLat ): 
            east = 180 
            west = 180- marginLat*2
        if(partialLong<=70-marginLong and partialLong>=-60+marginLong): 
            north= partialLong+marginLong
            south =partialLong-marginLong
        elif(partialLong<=70-marginLong and partialLong<-60+marginLong): 
            north= -60 +marginLong*2
            south =-60
        elif(partialLong>70-marginLong): 
            north= 70
            south =70 - marginLong*2

        northStr = str("%.6f" % north)
        southStr = str("%.6f" % south)
        eastStr = str("%.6f" % east)
        weastStr = str("%.6f" % west)
        if(num_answers==1 and player!=0): 
            r =requests.get('http://api.geonames.org/cities?north='+northStr+'&south='+southStr+'&east='+eastStr+'&west='+weastStr+'&maxRows=1'+'&username=fraart')
            resp =r.content
            tree = xml.etree.ElementTree.fromstring(resp)
            for child in tree.iter('*'):
                if(child.tag=="countryName" ): country.append(child.text)
                if(child.tag=="name"): city.append(child.text)
                if(child.tag=="lat" ): lat.append(child.text)
                if(child.tag=="lng" ): long.append(child.text)
            count+=1
        elif(num_answers==1 and player==0):
            r =requests.get('http://api.geonames.org/cities?north='+northStr+'&south='+southStr+'&east='+eastStr+'&west='+weastStr+'&maxRows='+str(level+1)+'&username=fraart')
            resp =r.content
            tree = xml.etree.ElementTree.fromstring(resp)
            for child in tree.iter('*'):
                if(child.tag=="countryName" ): country.append(child.text)
                if(child.tag=="name"): city.append(child.text)
                if(child.tag=="lat" ): lat.append(child.text)
                if(child.tag=="lng" ): long.append(child.text)
            count+=1
        else:
            r =requests.get('http://api.geonames.org/cities?north='+northStr+'&south='+southStr+'&east='+eastStr+'&west='+weastStr+'&maxRows='+maxRows+'&username=fraart')
            #http://api.geonames.org/countrySubdivision?lat='+lat+'&lng='+long+'&maxRows=1&radius=40&username=fraart'
            #http://api.geonames.org/cities?north=44.1&south=-9.9&east=-22.4&west=55.2&username=demo
            #https://api.3geonames.org/?randomland=yes
            resp =r.content
            #print(resp)
            tree = xml.etree.ElementTree.fromstring(resp)
            
            for child in tree.iter('*'):
                if(child.tag=="countryName" ): country.append(child.text)
                if(child.tag=="name"): city.append(child.text)
                if(child.tag=="lat" ): lat.append(child.text)
                if(child.tag=="lng" ): long.append(child.text)
            count+=1
            if(len(country)>=level+num_answers):
                country = country[level:]
                city = city[level:]
                lat = lat[level:]
                long = long[level:]
                i=0
                fake_country = country
                fake_city = city
                fake_lat = lat
                fake_long = long
                country=[]; city=[]; lat=[]; long=[]
                for elem in fake_country:
                    if(fake_country[i:len(fake_country)].count(elem)<2): 
                        country.append(elem)
                        city.append(fake_city[i])
                        lat.append(fake_lat[i])
                        long.append(fake_long[i])
                    i+=1
            elif(len(country)>=num_answers):
                country.reverse()
                city.reverse()
                lat.reverse()
                long.reverse()
                i=0
                fake_country = country
                fake_city = city
                fake_lat = lat
                fake_long = long
                country=[]; city=[]; lat=[]; long=[]
                for elem in fake_country:
                    if(fake_country[i:len(fake_country)].count(elem)<2): 
                        country.append(elem)
                        city.append(fake_city[i])
                        lat.append(fake_lat[i])
                        long.append(fake_long[i])
                    i+=1
    if(num_answers==1 and player!=0):return lat[0],long[0],country[0], city[0]
    elif(num_answers==1 and player==0):return lat[-1],long[-1],country[-1], city[-1]
    else: return lat[0],long[0],country[0:num_answers], city[0:num_answers]

   ###ADD LEVELS:
    #from 0  to n growth the difficulty selecting the lowest famous places
   ###3 SIMILAR ANSWERS AND 1 DIFFERENT
    
def randomizeLocation(lat,long):
    rangeLat = [0.007,-0.007]
    rangeLong = [0.01,-0.01]
    newLat = float(lat) + ( rangeLat[ random.randint(0,1) ]*random.random() )
    newLong = float(long) + ( rangeLong[ random.randint(0,1) ]*random.random() )
    return newLat, newLong


def findFirstAvailableID(list_game_id):
        game_id = 1000
        while(True):
            if(game_id not in list_game_id):
                return game_id
            else:
                game_id+=1