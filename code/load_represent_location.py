import requests
import time
import json
import csv

#driver=webdriver.Chrome('/home/ubuntu/workspace/p9_test/chromedriver')
def load_location(name_type, read_file_name, write_file_name):
#name_type : subway | weather
#google geocoding API를 활용하여, 해당 주소를 가지고 위도, 경도 좌표를 가져와, write_file_name(.csv)로 저장한다.
    address_group=[]
    f=open(read_file_name, 'r')
    csvReader=csv.reader(f)
    
    if(name_type=="subway"):
        for row in csvReader:
            if(row[0][-1]!='역'):
                row[0]=row[0]+"역"
            address_group.append(row[0])
    else:
        for row in csvReader:
            address_group.append(row[0])
    
    f.close()
    
    with open(write_file_name, 'w') as csvfile:
        writer=csv.writer(csvfile, delimiter=",")
        for address in address_group:
            based_url="https://maps.googleapis.com/maps/api/geocode/json?sensor=false&language=ko&address="+address+"&key=AIzaSyDkZLkvZzSeDGOhkwF5kgMShOq5QWuNigk"
        
            get_url=requests.get(based_url)
            time.sleep(1)
            
            json_data=get_url.json()
            print(json_data['status'])
            if(json_data['status']=="OK"):
                writer.writerow([address, json_data['results'][0]['geometry']['location']['lat'], json_data['results'][0]['geometry']['location']['lng']])
                print("%s latitude: %f" % (address, json_data['results'][0]['geometry']['location']['lat']))
                print("%s langtitude: %f" % (address, json_data['results'][0]['geometry']['location']['lng']))
        csvfile.close()
    
    return "SUCCESS"

def represent_location(object_data, compare_data, write_file_name):
# object파일과 compare 파일의 좌표를 비교하여, 추천 데이터를 도출한다.
# 시간복잡도 O(n*m)
    compare_group=[]
    file_object=open(object_data, 'r')
    file_compare=open(compare_data, 'r')
    file_write=open(write_file_name, 'w')
    writer=csv.writer(file_write, delimiter=",")
    csv_object=csv.reader(file_object)
    csv_compare=csv.reader(file_compare)
    
    for com in csv_compare:
        compare_group.append(com)
    
    for obj in csv_object:
        min_value=100
        min_location=''
        for com in compare_group:
            #print(obj, com)
            #print('-------')
            temp=pow((float(obj[1])-float(com[1]))+(float(obj[2])-float(com[2])),2)
            print(temp, min_value)
            if(temp<min_value):
                min_value=temp
                min_location=com[0]

        writer.writerow([obj[0], obj[1], obj[2], min_location, min_value])
    file_object.close()
    file_compare.close()
    file_write.close()
    
    return "SUCCESS"
    
if __name__=="__main__":
    
    load_location('weather', '/home/csxion/Desktop/project/data/p9_data_04_weather_list.csv', '/home/csxion/Desktop/project/data/p9_data_05_location_weather.csv')
    load_location('subway', '/home/csxion/Desktop/project/data/p9_data_03_subway_list.csv', 'home/csxion/Desktop/project/data/p9_data_06_location_subway.csv')
    represent_location('/home/csxion/Desktop/project/data/p9_data_06_location_subway.csv', '/home/csxion/Desktop/project/data/p9_data_05_location_weather.csv', '/home/csxion/Desktop/project/data/p9_data_07_represent_data.csv')   
    #print(s_data)

