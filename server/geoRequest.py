import requests
import random
import json
import time
import xml.etree.ElementTree 



api_key = 'AIzaSyCWSYohRYBIEvYkmjw79yqYXdlVTvXinrE'
#Reverse GeoCoding Request: https://maps.googleapis.com/maps/api/geocode/json?latlng=40.714224,-73.961452&key=YOUR_API_KEY

#Valid longitudes are from -180 to 180 degrees.
#Valid latitudes are from -85.05112878 to 85.05112878 degrees.

#parser = reqparse.RequestParser() #create the parser
def retrieveGeoInfo():
    lat, long, country, locality, city='','','','',''
    okResp= False
    goodCoord = False
    totTime=0
    while(not okResp): #filter zones with only sea
        latRange =[-180,+180]
        lonRange =[-60,+70]
        start_time = time.time()
        lat,long,city = chooseGoodCoordinates(latRange,lonRange)
        totTime+=time.time() - start_time
        #lat= str("%.6f" % random.uniform(latRange[0],latRange[1]) )
        #long = str("%.6f" % random.uniform(lonRange[0],lonRange[1]) )
        r =requests.get('https://maps.googleapis.com/maps/api/geocode/json?latlng='+lat+','+long+'&key='+api_key+'&language=it')
        resp = json.loads(r.text)
        if(r.status_code == 200 and resp['status'] =='OK'):
                         
            if(len(resp['results'])>0):
                okResp=True #to move 
                info = resp['results'][0]['address_components']
                #address = resp['results'][0]['formatted_address']

            else:
                okResp=False
                goodCoord=False
                break

            i=0
            while(i < len(info)):
                #if(info[i]['types'][0]!= 'postal_code'):
                places = info[i]['types'][0]

                if(places == 'country'):
                    country = info[i]['long_name']

                #elif(places == 'administrative_area_level_1'):
                #    adm_a_lev1 = info[i]['long_name']
                
                #elif(places == 'administrative_area_level_2'):
                #    adm_a_lev2 = info[i]['long_name']
            
                #elif(places == 'sublocality'):
                #    sublocality = info[i]['long_name']
                
                #elif(places == 'neighborhood'):
                #    neighborhood = info[i]['long_name']
                
                elif(places == 'locality'):
                    locality = info[i]['long_name']
                
                #elif(places == 'point_of_interest'):
                #    pointOfInterest = info[i]['long_name']

                #elif(places == 'establishment'):
                #    lat, address, long, country, locality,adm_a_lev1,adm_a_lev2,neighborhood,sublocality='','','','','','','','',''
                #    okResp = False
                #    break
                    #establishment = info[i]['long_name']
                i+=1

            ##print('--------------------------')
            if (country =='' or locality==''):
                okResp=False   
                goodCoord=False     
                lat, long, country, locality='','','',''
            #print(info)
            ## else: print(resp['status'])

        elif((r.status_code == 200 and resp['status'] !='OK')):
            okResp = False
            goodCoord = False
            ##print('Error: '+str(resp['status']))
            
        else: 
            okResp = False
            goodCoord = False
            ##print('Error: '+str(r.status_code))
    
    #print(country,adm_a_lev1,adm_a_lev2,neighborhood,locality,sublocality)
    print("filtering: "+str(totTime))
    return lat, long, country, locality,city



def retrieveSimilarAnswers(sourceCountry, sourceLocality):
    answers=[]
    '''minLatMarginRange=[-20,+20]
    minLongMarginRange=[-7,+7]
    latARange = [-180, lat+minLatMarginRange[0]]
    latBRange = [lat+minLatMarginRange[1],+180]
    latRange,lonRange =[],[]

    lonARange = [-60, long+minLongMarginRange[0]]
    lonBRange = [long+minLongMarginRange[1],+75]

    if((latARange[1]-latARange[0]) > (latBRange[1]-latBRange[0])):
        latRange = latARange  
    else:
        latRange = latBRange

    if((lonARange[1]-lonARange[0]) > (lonBRange[1]-lonBRange[0])):
        lonRange = lonARange  
    else:
        lonRange = lonBRange'''
    
    i=0

    while(i<3):
        lt, lg, country,locality, city = retrieveGeoInfo()
        if( sourceCountry!=country):
            answers.append({'country':country,'locality':locality, 'city':city})
            i+=1
        

    return answers



def chooseGoodCoordinates(latRange,lonRange):
    r =requests.get('https://api.3geonames.org/?randomland=yes')
    resp =r.content
    tree = xml.etree.ElementTree.fromstring(resp)
    
    lat,long,city ='','',''

    for child in tree.iter('*'):
        if(child.tag=="latt"): lat= child.text
        if(child.tag=="longt"): long= child.text
        if(child.tag=="city"): city= child.text
        if(child.tag=="state"): state= child.text 
        
    return lat,long, city
    '''lat,long = '',''
    goodCoord=False

    while(not goodCoord):
            partialLat= random.uniform(latRange[0],latRange[1])
            partialLong = random.uniform(lonRange[0],lonRange[1])
            if( ( partialLat>-45 and partialLong<-40  ) #remove southern ocean
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

            else:
                goodCoord = True
                lat = str("%.6f" % partialLat)
                long = str("%.6f" % partialLong)

    return lat, long'''
   # print(lat,long)
   
    

