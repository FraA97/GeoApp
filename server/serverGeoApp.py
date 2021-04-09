from geoRequest import randomizeLocation, retrieveGeoInfo, retrieveSimilarAnswers 
from flask import Flask
from flask_restful import Api,Resource,reqparse   #for parse request and replies automatically    
            

app = Flask(__name__)
api = Api(app) 


'''import time
start_time = time.time()


lat, long, country,locality,city= retrieveGeoInfo()

print("lat: "+lat, "long: "+long,"country: "+country, "locality: "+ locality, "city: "+city )

print("----------------------------------")
print(retrieveSimilarAnswers( country,locality))

print("--- %s seconds ---" % (time.time() - start_time))'''

#kinds of get requests:
INIT_STATE = 0 
    #GET0: req=0 [required] + game_id="1234567" [not required (IDENTIFY THE GAME)](sended by FIRST player)
        #return id player for the game
              #+WAITING=true
              #+ set STATE = SYNC_STATE
    
    #GET0: req=0 [required] + game_id="1234567" [ (IDENTIFY THE GAME started by player 2)
         #return id player for the game 
         #+set WAITING =false
         # + set STATE = PLAY_STATE
SYNC_STATE = 1 
    #GET1: req=1 [required] + game_id="1234567" //polling on variable WAITING
        #return |||WAITING (=true) (if waiting other player ) or WAITING( =false) (player2 is ready)
        #IF WAITING== false => set STATE = PLAY_STATE
  
  
PLAY_STATE = 2
    #GET2: req=2 [required] + game_id="1234567" [ required] + player_id //PLAYER 1 WANT START TO PLAY 
        #set wait_next_lev =false 
        #return: geo coordinates and names
        # + set STATE = LEVEL_STATE
    
    #polling:
    #GET2: req=2 [required] + game_id="1234567" [ required] + player_id //PLAYER 2 do polling  
        #polling on wait_next_lev variable
        #return:  wait_next_lev variable 
                # (if false => +geo coordinates and names)       
LEVEL_STATE = 3
    #GET3: req=3 [required] + game_id="1234567" //NOTIFY THAT A PLAYER HAS COMPLETED THE LEVEL
        #SET |||WAITING (=FALSE) (if is the second req=3 with the same game_id ) 
        #      or WAITING( =true) (if is the first req=3 with the same game_id ) 

WAITING_STATE = 4
    #GET4: req=4 [required] + game_id="1234567" //polling on variable WAITING
        #return |||WAITING (=true) (if waiting other player ) or WAITING( =false) (player2 is ready)
        #IF WAITING== false => set STATE = PLAY_STATE
INTERRUPT_STATE = 5
ID_PLAYER = -1




STATE = INIT_STATE

list_game_id = []
coordinates_game = {}
num_req = {}
sync = {}
waiting = {}
random_dict = {}
score_dict = {}
interrupt_dict = {}
num_levels_dict = {}
master_pl_left_dict = {}
name_players_dict = {}
parser = reqparse.RequestParser()

class GeoApp(Resource):    #Resource for use Crud op and other...
   
    def get(self):
        parser.add_argument('req',type=int,required = True)
        parser.add_argument('game_id',type=int,required = False)
        parser.add_argument('player_id',type=int,required = False)
        parser.add_argument('random',type=int,required = False)#=0=> False|| =1=>True
        parser.add_argument('level',type=int,required = False)#number of current level s.t. can be decided the correspondent difficulty
        parser.add_argument('num_levels',type=int,required = False)
        parser.add_argument('score',type=int,required = False)#score of each player
        parser.add_argument('user_name',type=str,required = False)
        parser.add_argument('interrupt',type=int,required = False)
        args = parser.parse_args() #parse the msg

        req = args['req'] #0||1||2||3||4
        game_id = args['game_id'] #0||1||2||3||4......
        player_id = args['player_id']
        random = args['random']
        level = args['level']
        num_levels = args['num_levels']
        score = args['score']
        user_name = args['user_name']
        interrupt = args['interrupt']

        if(req == INIT_STATE): #ALWAYS: req=0 + user_name+ SOMETIMES: game_id + random
            if(game_id == None):
                if(random == None or random==0): #not random game
                    g_id = len(list_game_id)
                    list_game_id.append(g_id)
                    random_dict[g_id] = False
                    sync[g_id]=0
                    num_req[g_id] = 0
                    name_players_dict[g_id]=[]
                    name_players_dict[g_id].append(user_name)
                    print(name_players_dict[g_id])
                    #print(num_levels)
                    #print("------------")
                    if(num_levels == None or num_levels==0):
                        num_levels_dict[g_id] = 3
                    else:
                        num_levels_dict[g_id] = num_levels
                    interrupt_dict[g_id]= 0
                    master_pl_left_dict[g_id] = False
                    return {"error":False, 'msg':"return game_id and id_player", 'game_id':g_id,'player_id':0}
                elif(random!= None and random==1): #random game                   
                    found=False
                    for key in random_dict:
                        if random_dict[key]:
                            found = True
                            g_id = key
                            sync[g_id]+=1
                            random_dict[g_id] = False
                            name_players_dict[g_id].append(user_name)
                            print(name_players_dict[g_id])
                            return {"error":False, 'msg':"return game_id and id_player", 'game_id':g_id,'player_id':1}
                        
                    if(not found):   
                        g_id = len(list_game_id)
                        list_game_id.append(g_id)
                        random_dict[g_id] = True
                        sync[g_id]=0
                        num_req[g_id] = 0
                        interrupt_dict[g_id] = 0
                        master_pl_left_dict[g_id] = False
                        name_players_dict[g_id] =[]
                        name_players_dict[g_id].append(user_name)
                        print(name_players_dict[g_id])
                        return {"error":False, 'msg':"return game_id and id_player", 'game_id':g_id,'player_id':0}
                
            else: #exist game_id means that no random game
                if(game_id in sync and not random_dict[game_id] and game_id in num_levels_dict):
                    sync[game_id]+=1
                    name_players_dict[game_id].append(user_name)
                    return {"error":False, 'msg':"return game_id and id_player", 'game_id':game_id,'player_id':sync[game_id],'num_levels':num_levels_dict[game_id]}
                else:
                    return {"error":True, 'msg':"Error: game_id = "+str(game_id)+" not exist or missing num_levels"}
        
        elif(req == SYNC_STATE):
            if(game_id not in sync):
                return {"error":True, 'msg':"Error: game_id = "+str(game_id)+" not exist"}
            elif(game_id not in name_players_dict):
                return {"error":True, 'msg':"Error: name_players_dict not yet initialized = "+str(game_id)}
            #elif(sync[game_id]):
             #   STATE = PLAY_STATE
              #  return {"error":False, 'msg':"return waiting state", 'waiting':True}
            #elif(not sync[game_id]):
            #    STATE = PLAY_STATE
            #    return {"error":False, 'msg':"return waiting state", 'waiting':False}
            else:
                STATE = PLAY_STATE
                return {"error":False, 'msg':"return number of synchronized players (you excluded)", 'num_sync_pl':sync[game_id],
                        'num_pl_left':interrupt_dict[game_id],'name_players':'-'.join(name_players_dict[game_id])}
        
        elif(req==PLAY_STATE):
                waiting.pop(game_id, None)
                if( (level==None and game_id==0) or game_id==None or player_id==None ):
                    return {'error':True, 'msg':"missing parameters [level and/or game_id and/or player_id"}
                
                if( (game_id in list_game_id) and player_id==0 and num_req[game_id]==0):
                    num_req[game_id] +=1 
                    lat, long, country,city = retrieveGeoInfo(level,3,None,1) 
                    lat, long = randomizeLocation(lat,long)
                    f_lat,f_long,false_country,false_city = retrieveGeoInfo(level,1,country[0],1) 
                    false_answers = retrieveSimilarAnswers(country,city,false_country,false_city)
                    
                    coordinates_game[game_id] = [lat,long,country,city,false_answers]
                    STATE = LEVEL_STATE
                    return {'error':False, 'msg':"return true answer and the three false answers", 
                                'Country' :country[0],'City':city[0], 'lat' :lat,'long':long,
                                'fCountry1':false_answers[0]['country'],'fCity1':false_answers[0]['city'],
                                'fCountry2':false_answers[1]['country'],'fCity2':false_answers[1]['city'],
                                'fCountry3':false_answers[2]['country'],'fCity3':false_answers[2]['city']}

                elif((game_id in list_game_id) and player_id>0 and player_id<=sync[game_id] and num_req[game_id]>0 ):
                    if(game_id in coordinates_game):
                        if(num_req[game_id]==sync[game_id]+1): num_req[game_id] = 0 #if all player have done their request
                        #print(coordinates_game[game_id])
                        lat,long,country,city,false_answers=coordinates_game[game_id]
                        #coordinates_game[game_id]=None
                        return {'error':False, 'msg':"return true answer and the three false answers", 
                                    'Country' :country[0],'City':city[0], 'lat' :lat,'long':long,
                                    'fCountry1':false_answers[0]['country'],'fCity1':false_answers[0]['city'],
                                    'fCountry2':false_answers[1]['country'],'fCity2':false_answers[1]['city'],
                                    'fCountry3':false_answers[2]['country'],'fCity3':false_answers[2]['city']}
                    else:
                        return {'error':True, 'msg':"wait the master player"}
                elif((game_id in list_game_id) and player_id>0 and num_req[game_id]==0):
                    return {'error':True, 'msg':"wait the master player"}
                    
        elif(req == LEVEL_STATE):
            if(score==None  or game_id==None or user_name==None):
                return {'error':True, 'msg':"missing parameters [score and/or game_id  and/or user_name]"}
            if(game_id not in waiting): waiting[game_id] = 0
            else: waiting[game_id] += 1#False
        
            if game_id not in score_dict: score_dict[game_id] ={}
            score_dict[game_id][user_name] = score
            return {"error":False, 'msg':"level completed", 'game_id':game_id, 'number of players to wait': sync[game_id]-waiting[game_id]-interrupt_dict[game_id]}
        
        elif(req == WAITING_STATE):
            coordinates_game.pop(game_id, None)

            if(game_id not in waiting): return {"error":False, 'msg':"return waiting_state", 'waiting': True}
            if( sync[game_id]-waiting[game_id]-interrupt_dict[game_id] <= 0): 
                #print(sync[game_id]-waiting[game_id]-interrupt_dict[game_id])
                STATE = PLAY_STATE
                num_req[game_id]=0
                #waiting.pop(game_id, None)
                return {'error':False, 'msg':"return waiting_state, total_score and players_left", 
                        'waiting': False, 'total_score':score_dict[game_id],'num_pl_left':interrupt_dict[game_id],
                        'master_pl_left':master_pl_left_dict[game_id]}
            else:
                num_req[game_id]=0 
                return {"error":False, 'msg':"return waiting_state", 'waiting': True}

        elif(req == INTERRUPT_STATE): #required parameters: game_id, player_id, interrupt=1, user_name
            if(interrupt==1 and game_id in interrupt_dict and player_id!=None):
                interrupt_dict[game_id]+=1
                if(player_id==0): master_pl_left_dict[game_id] = True
                name_players_dict[game_id].remove(user_name)
                return {"error":False, 'msg':"game stopped"}
            elif(interrupt!=1):
                return {"error":True, 'msg':"interrupt != 1"}
            elif(game_id not in interrupt_dict):
                return {"error":True, 'msg':"game id not exist"}
            elif(player_id==None):
                return {"error":True, 'msg':"insert player id"}
            else:
                return {"error":True, 'msg':"ERROR"}

api.add_resource(GeoApp,"/")

if __name__ == "__main__":
    print("starting api...")
    #app.run()
    #app.run(host = "0.0.0.0")
    from waitress import serve
    serve(app, host="0.0.0.0", port=8080)

