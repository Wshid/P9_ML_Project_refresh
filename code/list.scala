val raw_text=sc.textFile("/home/ubuntu/workspace/project/download/raw_data/2016_subway/subway_list.csv")
raw_text.distinct().saveAsTextFile("/home/ubuntu/workspace/project/download/raw_data/2016_subway/test.csv")

val raw_text=sc.textFile("/home/ubuntu/workspace/project/download/raw_data/2016_weather/weather_list.csv")
raw_text.distinct().saveAsTextFile("/home/ubuntu/workspace/project/download/raw_data/2016_weather/test.csv")
