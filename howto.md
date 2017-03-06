1. 기상데이터와 기름값 데이터를 합친다. => lookup계열(vlookup)의 함수를 이용
    ```
    1601_weather_price // 1601_weather_csv + 16_oil_price
    ```

2. subway데이터에서 모든 목록을 distinct화해서 역 리스트를 출력한다.(p9_data_03_subway_list.csv)
        ```
        val raw_data=sc.textFile("/home/csxion/Desktop/project/data/p9_data_00_16_all_1d_subway.csv")
	val subway_list=raw_data.map(_.split(",")(0))

        subway_list.distinct().saveAsTextFile("/home/csxion/Desktop/project/data/p9_data_03_subway_list")
        ```

3. weather에서 distinct를 이용하여 기상 관측지점 리스트를 출력한다.(p9_data_04_weather_list.csv)
        ```
val raw_data=sc.textFile("/home/csxion/Desktop/project/raw_data/2016_weather/1601_weather.csv")
val weather_list=raw_data.map(_.split(",")(1))

weather_list.distinct().saveAsTextFile("/home/csxion/Desktop/project/data/p9_data_04_weather_list")
        ```

2. 역명에 따른 기상데이터 포인트 추천 데이터를 생성한다.
    ```
    load_represent_location.py 이용
    ```


2. subway데이터에서 첫 행에 해당하는 데이터를 제외시킨다.
    ```
    sed -1d 1601_weather.csv > 1601_sed.csv
    ```

3. 부분합(partial_sum) 데이터를 생성한다. 이 작업은, 할인 또는 상/하차의 구분을 없애기 위해 사용한다.
    ```
    1601_03_partial_sum.csv // code_partial_sum을 이용
    이때, 할인 열은 제거된다.
    ```

4. partial_sum.csv 파일을 엑셀을 통하여 정제한다
    ```
    역명, 날짜, 시간데이터에 불순물이 담겨져있는데, 잘 수정한다
    헤더 데이터를 복구시킨다.(raw_data에 헤더를 복사하여 붙여넣는다.)
    ```

weather_price에 공란이 많음, 값을 넣을것
(온도, 강수량 등의 정보가 비어있음)

4. 엑셀 작업을 통하여 열데이터 였던 시간데이터를 세부 행데이터로 전환시킨다.
    ```
    1) 다른 열에 각 행마다 20씩의 숫자가 차오르는(20,40,...)데이터를 생성한다.
    2) 생성되는 20배수 데이터의 최대값을 확인한 후, 다음 열에 1씩 그 데이터 만큼 채우기 핸들을 한다
    3) 정렬 기능을 활용하여 각 셀마다 약  20개의 행씩 포함될 수 있도록 한다.
    4) 날짜 열의 모든 데이터를 선택한후 F5키를 누르고 빈 셀을 클릭한다.
    5) 이전 행을 참조하는 구문(=A2)을 작성한후, Ctrl+Enter키를 누른다.
    6) 생성된 날짜데이터를 전부 복사한뒤, 선택하여 붙여넣기-값을 한다.
    7) 시간데이터도 날짜데이터와 동일한 작업을 실시한다.
    8) subway 시트와 현재작업시트 모두 태그열 (역명+날짜) 을 구성한다.
    9) index(array), match함수를 활용하여 인원 행에 데이터를 추가한다.(행 -> 열 전환작업 본문)
    10) 태그열(날짜+시간+역명)으로 구성되는 데이터를 생성한다.
    11) 해당 태그에 맞게 lookup함수를 활용하여 날짜/가격 + subway 데이터를 통합시킨다.
    ```

5. 인원에 해당하는 값을 가장 마지막 열로 옮긴다(labelPoint시, 값으로 사용)
6. 수식-오류검사 를 진행하여, 누락되거나 수식 에러가 난 부분을 처리한다
