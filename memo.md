17/02/04    대부분의 설치 완료
            jupyter에 scala 올리기 부터 시작하면 됨
        
17/02/06    http://gyrfalcon.tistory.com/entry/Spark-S넴park-Standalone-%EC%84%A4%EC%B9%98 참고
            https://p9-ml-project-csxion.c9users.io/
            ```
            MASTER DIR : sbin/start-master.sh 
            SLAVE DIR : sbin/start-slave.sh spark://csxion-p9-ml-project-4374433:7077
            $SPARK_SLAVE_HOME/sbin/start-slave.sh spark://csxion-p9-ml-project-4374433:7077 -c 1 -m 1024m    
            ```
            readonly 발생시, 그냥 검색해서 해결할 것
            
            http://www.slideshare.net/KangDognhyun/2apache-spark

        mvn archetype:generate -DgroupId=net.kjunine -DartifactId=sample -Dpackage=net.kjunine.sample -Dversion=1.0-SNAPSHOT
        
        spark-submit --master spark://csxion-p9-ml-project-4374433 --jars test_first-1.0-SNAPSHOT.jar
    -> 이게 왜 에러가 나지
    Exception in thread "main" java.lang.IllegalArgumentException: Missing application resource.
    
            //spark ml 책 활용하여 구문 변경할 것

    다양한 방법을 시도하였으나, 해결되는 방법은 없었음
    mvn으로 build하는 법은 잠시 접어두고, sbt로 진행할 것

    sbt에 관한 정보 : http://www.bench87.com/content/7
                        https://blog.outsider.ne.kr/565
    sbt assembly -> jar 파일을 만드는 명령어
    build.sbt에 내부 내용 추가(러닝스파크 p153 참고)
    
    계속 에러가 발생함
    https://github.com/sbt/sbt-assembly/issues/153
        -> 해결 링크 : https://github.com/sbt/sbt-assembly/blob/master/Migration.md#upgrading-with-bare-buildsbt
                        https://github.com/sbt/sbt-assembly
    assembly에 대한 설정이 자동으로 추가된다고 함
    
    sbt assembly 에러
    provided를 libraryDependencies에 추가함으로써, dependency 충돌을 막을 수 잇음
        -> https://github.com/sbt/sbt-assembly#excluding-jars-and-files
        
     Merging 'META-INF\INDEX.LIST' with strategy 'discard'
        -> mainClass in assembly := Some("com.example.Main")
    
    일단 merging 에러까지는 무시하는데
    
    생각해보면 spark-submit 자체에서 문제가 생기는 건지
    계속 Missing Application resource가 뜸
    
    
    
    http://spark.apache.org/docs/latest/quick-start.html 이거 확인해보기
    
    
     missing argument list for method set in class SparkConf
     이거 에러 뜸 확인할 것
     
     class 옵션을 넣지 않으니까, 정상작동함
     
17/02/07     
    sparkconf 설정하는 방법은 여러가지가 있음
     
     1. new SparkConf().setMaster 와 같이 설정하는 방법
     
     2.
         conf=new SparkConf()
         conf.set('spark.app.name', 'first_app')
     
    worker node의 용량을 1024m로 두고
    submit의 executor-용량을 512m으로 두면 정상 실행됨
    
    log4j.properties WRAN,console로 내용변경 한뒤
    submit이후 결과 콘솔에서 확인함
    
    $SPARK_HOME/work 에서 각 work별 stdout, stderr를 확인할 수 있음
    http://spark.apache.org/docs/latest/spark-standalone.html 참고
        -> stdout에서 내용 출력이 안됨.. 원래 이런건가
    
    일단 sbt 빌드 및 실행까지는 완료함. 자세한 내용은 더 찾아보기로
    
    codenvy에서 설치 확인해볼것
    
17/02/09
    locale 및 tzselect 설정 완료
    승하차인원통계 : https://www.data.go.kr/dataset/15003169/fileData.do
        csv 파일 다운받아서 1차 정제 완료 => modified_1601 ~ 1612
            각 열별 이동 및 제거
                1~4, 5, 6~12의 경우 내용 포맷이 조금씩 다름
                1~4     할인 : 일반, 어린이,.. 등으로 세부적으로 나누어짐
                5       승/하차, 할인 : 두개의 구분자를 사용
                6~12    승/하차 : 승차 및 하차로만 구분함
            
            그루핑할때 참고해야 할 듯
            

17/02/10
    du : 리눅스 디렉토리 용량 파악 // http://zetawiki.com/wiki/%EB%A6%AC%EB%88%85%EC%8A%A4_%EB%94%94%EB%A0%89%ED%86%A0%EB%A6%AC_%EC%9A%A9%EB%9F%89_%ED%99%95%EC%9D%B8_du
    
    rm -rf /tmp/*
    sudo apt-get install ncdu
            
    du-c9 | sort -h
    
    https://data.kma.go.kr/data/grnd/selectAsosRltmList.do?pgmNo=36 // 기상 데이터 조회
    
    지하철의 이용자 수는 대략 기름값, 날씨 등의 영향을 받을 수 있지 않을까?
    
    oil_price_16 : 16년도 기름값 데이터 // http://www.opinet.co.kr/user/dopospdrg/dopOsPdrgSelect.do#
    대기오염 정도 : ..
    날씨 데이터 : http://sts.kma.go.kr/jsp/home/contents/statistics1/newStatisticsSearch.do?menu=AWS&MNU=MNU
                    1~6월까지의 데이터 수집 완료
                    

17/02/11
        날씨데이터 7월 ~ 12월 수집하기
            DB연결 초과시, 브라우저 변경하면서 시도 및 성공
        
        역과 지역을 매칭 시켜야함
        
        weather data에 대한 pyspark 테스트 실시
        해당 주석부터 다시 시작하면 됨
        
        plt에 대한 그래프 그리는 방법에 대해 숙지하기
        
17/02/12
        Spark ML 책에서 챕터 5,6 참고해서 작업 실시 예정
    
17/02/13
        한셀을 이용하여 1월달 날씨+오일데이터 취합 완료
        부분합 사용시, 리소스 초과로 인한 에러 발생하는 듯함
        spark-error 발생, 새로운 워크스페이스 구성 예정
        
        
        datanucleus 에러의 경우, ln -s 로 사용하던 기존환경에 대해 에러가 있었음
            symbolic link를 제거한 후, 원본을 참조하게 한 뒤,
            디렉터리명에 -와 같은 특수문자를 제거해주면 정상적으로 참조함
        
        metastore_db의 경우 spark version이 올라가면서 hadoop과의 추가 객체가 생긴듯 함. 무시하면 될 듯
        
        환경 정상화 완료
        
        http://mygeoposition.com/?query=%EC%84%9C%EC%9A%B8%EC%97%AD&accuracy=6#geodata // GPS 데이터 수집 가능, 파싱하기도 쉬울듯
        bot project를 clone하여 프로젝트 생성
        
        
        beautifulsoup에서도 동적 크롤링이 가능함
        http://hashcode.co.kr/questions/2039/beautifulsoup%EC%9C%BC%EB%A1%9C-%EC%9B%B9%ED%81%AC%EB%A1%A4%EB%A7%81-%ED%95%A0%EB%95%8C-%EB%8D%B0%EC%9D%B4%ED%84%B0%EB%A5%BC-%EB%AA%BB-%EC%9D%BD%EC%96%B4%EC%98%A4%EB%8A%94%EA%B1%B4-%EC%96%B4%EB%96%BB%EA%B2%8C-%ED%95%B4%EA%B2%B0%ED%95%A0%EC%88%98%EC%9E%88%EC%9D%84%EA%B9%8C%EC%9A%94
        
        
        selenium 설치 완료 -> 동적 페이지 크롤링
        http://blog.naver.com/dalsun75/220483407779 -> 크롬 드라이버 사용법
        
        크롬드라이버가 제대로 실행이 되지 않음. 이유 확인할 것
        
        sudo apt-get install chromium-browser.. 하... 뭔가 안되는듯 한데
        
        파이썬은 포기, 다른 파서를 찾아보기로
        http://kin.naver.com/qna/detail.nhn?d1id=1&dirId=1040203&docId=267376875&qb=7J6Q67CU7Iqk7YGs66a97Yq4IO2MjOyLsQ==&enc=utf8&section=kin&rank=7&search_sort=0&spq=0 // 이거하면 됨
        
        안된다 그냥
        python의 beautifulsoup의 경우, 동적 파싱을 하기 위해서는 Webdriver가 필요한데, 이는 cloud9에서 동작하지 않는다.
        jsoup의 경우도 상황은 마찬가지,
        
        다른페이지를 찾았더니, 네이버 open api를 사용하라고 한다.
            사용 신청을 하려고 했으나, 휴대폰 인증을 해야하기 때문에, 바로 휴대폰 사용이 불가한 나로써는 이 방법 마저 불가능하다.
        
        조금 찾아보다가 안되면 수동으로 작업 시작해야겠다.
        
        수동으로 합시다. googledocs내용 부터 확인
        
        멘붕 제대로 옴 spark도 운용이 제대로 안되니까..
        일단 파일 수정해서 tsv파일로 변경 => 1601_rawdata
        이 파일을 가지고 reducebykey를 적용하기
        
        csv -> tsv 도중 데이터 일정 부분이 변경되지 않는 에러 발생, 해결 함
        
        원본데이터 문제인건지.. 계속 에러 발생
            -> 데이터내에 빈칸이 많았음. 엑셀로 확인하여 0으로 채움

17/02/15
        파싱하는법 알아냄   phantomjs 사용하기
        설치 참고 페이지    http://programmingsummaries.tistory.com/365
        ```
        https://bitbucket.org/ariya/phantomjs/downloads/ 최신 파일 확인, wget을 이용한 다운로드
            wget https://bitbucket.org/ariya/phantomjs/downloads/phantomjs-2.1.1-linux-x86_64.tar.bz2
            sudo tar -xvjf phantomjs-2.1.1-linux-x86_64.tar.bz2
            sudo mv phantomjs-2.1.1-linux-x86_64 /usr/local/share/
            
            sudo ln -sf /usr/local/share/phantomjs-2.1.1-linux-x86_64/bin/phantomjs /usr/local/share/phantomjs
            sudo ln -sf /usr/local/share/phantomjs-2.1.1-linux-x86_64/bin/phantomjs /usr/local/bin/phantomjs
            sudo ln -sf /usr/local/share/phantomjs-2.1.1-linux-x86_64/bin/phantomjs /usr/bin/phantomjs
            
            $ sudo apt-get install fonts-unfonts-core
            $ sudo apt-get install fonts-unfonts-extra
        ```
        
        test.js
        ```
        var page = require('webpage').create();
        system = require('system');
        var fs = require('fs');// File System Module
        
        var args = system.args;
        var url = args[1];    // 대상 webpage url
        var output = args[2]; // 저장할 파일이름, path for saving the local file 
        
        page.open( url, function() { // open the file 
          fs.write(output,page.content,'w'); // Write the page to the local file using page.content
          phantom.exit(); // exit PhantomJs
        });
        ```
        
        phantomjs test.js [URL 주소] 파싱 결과 파일
        
        html 결과를 많이 출력해야함 // 결과가 역별, 또는 지역별로 한개씩 출력될 것
        
        javascript의 반복문 사용 + python의 반복작업.. -> 의도치 않은 P1, P2 연계
        
        csv파일 저장 방법
            rdd.saveAsTextFile("[PATH]") // 각 파티션별로 저장이 됨
            
        "역"이라는 명칭을 어디다 둬야 할까
            파싱 직전에 바꿔야 하는게 맞는것 같기도 하고
            아닌것 같기도 하고
        바꿔서 list데이터를 만들게 되면, 추후 참조할때 에러가 날 것같음
        js 파싱 직전에 "역"이라는 명칭을 만들어 주기로
        
        ```
        val raw_text=sc.textFile("/home/ubuntu/workspace/project/download/raw_data/2016_subway/subway_list.csv")
        raw_text.distinct().saveAsTextFile("/home/ubuntu/workspace/project/download/raw_data/2016_subway/test.csv")
        ```
        폴더가 하나 생성되며, 파티션별로 저장된다.
        
        .sh파일을 입력해서 해야지 뭐..
        쉘파일에서 해당 csv파일을 읽은 후
        여러 명령어를 자동적으로 생성, 파일을 출력한다.
        
        
        애매한게 메인페이지는 js 받아서 파싱이 되는데..
        query만 입력하면 위/경도 정보가 없음
        아무래도 걸리는 시간때문인듯 한데 모르겠다
        확인해볼것
        
17/02/17
        google api 사용하기 위해, 프로젝트 생성 및 api 사용 인가
        키를 발급받긴 했지만, 아직까지 사용법이 미숙함
        
        json을 이용한 요청, 응답을 받아야 하기 때문에, jquery내 ajax를 활용한
        교신을 통하여 값을 가져오는게 좋을 듯
        
        사지방에서 구글 서비스 자체가 원활하지 않기 때문에, 휴가때 진행하는 걸로 해야할 듯
        
17/02/25
        jquery 및 ajax함수를 통한 parsing 진행시,
        하나 하나씩의 값 받아오는건 상관이 없으나,
        getJSON의 비동기식 실행으로 인하여, 반복문 구현시 애로사항 발생
        
        python에 있는 requests를 이용하여 실행시, 정상작동하는 것을 확인
        
        csv에 있는 역명을 읽어와 리스트로 저장한후, 
        그 내용에 맞게 검색 후 리턴하는 코드를 작성하면 될듯
        
        python으로 역별 데이터 입력 및 출력 완료
        csv파일을 읽어들인후, 
        역이름,위도,경도로 csv파일을 저장 => location_subway.csv
        
        날짜데이터에서도 동일 함수를 적용하기 위해서 RDD에서 지점 이름 따내기
        
        일단 원본 날짜데이터에서 지점이름만을 따내기 위해, excel을 이용한다
        => weather_list.csv
        ```
        val raw_text=sc.textFile("/home/ubuntu/workspace/project/download/raw_data/2016_weather/weather_list.csv")
        raw_text.distinct().saveAsTextFile("/home/ubuntu/workspace/project/download/raw_data/2016_weather/test.csv")
        ```
    
        역별 추천데이터 형성 완료
        
        01_weather_price에서 기온 및 풍속의 공란 데이터가 많음
            (이전행+다음행)/2로 계산, 최신화 완료
        
        데이터 중에 ,로 구분하게 되면 천의 자리에서 끊기는 경우가 생김
        엑셀로 데이터를 옮긴뒤, 회계옵션을 설정했다 품으로써 해당 ���제 해결
        
        pyspark를 이용하여 부분합 성공,
        하지만, 파일 저장시 키, 값이 그대로 텍스트로 저장되므로, 엑셀을 통하여 정제하기
        
        정제를 완료해서 확인한거지만, 실제로 정확히 취합되지 않은 자료들이 있음
            동대문역사문화공원역, 을지로3가역등 2개로 나눠서 취합됨. 이유가 뭘지는..
        
        시간별 데이터 열에서 행으로 전환하기
        하는 방법이 도저히 없는 것같아 엑셀에서 편법을 사용하도록 한다.
        http://ttend.tistory.com/521
        필요한 작업 링크 : http://sasbigdata.com/entry/EXCEL-%EC%97%91%EC%85%80-%EA%B3%B5%EB%B0%B1-%EC%9E%90%EB%8F%99-%EC%B1%84%EC%9A%B0%EA%B8%B0%EB%8C%80%ED%91%9C%EA%B0%92-%EB%8D%B0%EC%9D%B4%ED%84%B0%EC%99%80-%EA%B0%99%EA%B2%8C-%EB%AA%A8%EB%91%90-%EC%B1%84%EC%9A%B0%EB%8A%94-%EB%B0%A9%EB%B2%95
        
        3가지가 일치할때의 정보를 구해야함
        단순 함수로 구현이 안되기 때문에, 행 데이터를 태그로 합친후, index, match를 사용하여 구하기로 한다
        
        기상 + 날씨/유가
            마찬가지로 태그를 생성한뒤, 값을 제대로 참조할 수 있도록 함수를 사용한다.
        
        리소스를 많이 먹는 작업이다.
        
        통합데이터 생성 완료

17/02/26
        마포 15~31일데이터가 없다 확인해야 할 듯
        
        weather_price에 마포 관련 데이터 추가 및 적용 완료
        
        마포데이터가 상당히 불안정함
            18일 18시 이후 기록이 없음
            14일 기록도 일부 손실됨
        마포라는 지역을 제거하여 시도해야할 듯
        raw_data 다시 생성하기 많이 깨진듯
        일단 지금까지 했던 것 메뉴얼로 구성하기
        
        howto.md으로 하는 방법 정리
        날씨데이터에서 '마포' 데이터를 제거하여 다시 구성하기
        
        workspace 구성
            code : 실제 데이터 코드 및 이용 데이터
            raw_data : 원본 데이터
            data : 가공된 데이터
        
        데이터 재구성 완��� 및 1601_04_merge.csv 파일 완료
        엑셀데이터로 각 월별로 구분할 예정
        
17/02/27
        1601에 대하여
        master및 worker node의 실행 및 실질적인 어플리케이션 코드 작성 후 테스트 시작
        ```
        MASTER DIR : sbin/start-master.sh 
        SLAVE DIR : sbin/start-slave.sh spark://csxion-p9-ml-project-4374433:7077
            $SPARK_SLAVE_HOME/sbin/start-slave.sh spark://csxion-p9-ml-project-4374433:7077 -c 1 -m 1024m  
        
        build.sbt => libraryDependencies 설정
        spark_mkdir.sh 실행 후, main/scala/코드 작성
        sbt package
        spark-submit --master spark://csxion-p9-ml-project-4374433 model_first_2.11-1.0.jar 
        ```
        
        즉각적인 테스트를 위하여, spark-shell로 작성 한 후,
        세부 내용을 sbt application으로 제출하기로
        
        로그 옵션을 설정하기 위해서, 하단의 내용을 참고한다.
        --driver-java-options "-Dlog4j.configuration=file:///local/home/.../log4j.properties"
        
        model_first_shell이라는 파일 생성
            10000개까지는 에러가 나지 않지만, 20000개 가량 접근시, 에러 발생
            csv파일에서 고질적으로 나타나는 문제라고 하던데,
                csv파일이 제대로 나누어 지지 않았거나, 값이 없거나 하는 등의 에러임
                    => pyspark로 시도해보기 안되면.. 뭐....
            raw_data에서 깨진부분 발견, 이진검색방법으로 조회해가며 찾음
                => 엑셀의 오류검사 기능을 활용하면, 자동으로 잡아줌
        
        실제로 예측은 해가고 있으나 오차가 상당히 큼
        예측 모델 모두 생성후, 최적화 작업 실시하기

17/02/28
    예측값이 터무니 없음
    보통 LinearRegressionWithSGD의 경우 stepSize항목이 중요하다고 함. 이 값이 0.01만 되더라도 엄청나게 큰 값
    수렴할 수 있도록 변수를 조정해 주어야 함
    
    pyspark로 조정하여 입력값의 편차를 바꾸어보도록 하기
    
    pyspark에서 특징 최적화 하는 부분에서 에러 발생, 확인할 것

17/03/01
    각각의 에러수정 및 mse, 각 변수에 따른 rmsle값을 확인함
    DecisionTree 및  LinearRegressionWithSGD 모델 테스트 시작
    값중에 날짜데이터를 제거할 예정
        실제 회귀모델에서 날짜를 집어넣는 것은 모델 트레이닝상의 혼란이 가중될 것으로 판단
        날짜의 경우 각각 날씨마다 클러스터 모델에 적합할 듯
    
    책에 있는 내용과 유사하게 작업 완료
    raw_data를 변경한 후 다시한번 해봐야할듯, 오차가 너무 큼
    pyspark에서 커널이 속 끊어지는 에러 발생 // 하나씩 개별 실행하기로
    
    무엇이 문제인지 모르겠네.. 일정 수치에서 더 이상 변하지 않음
    
    원본 데이터의 문제, 실제 머신러닝에 활용되는 데이터들은 전부 정규화를 거친 데이터를 사용하였음(p223)
    각 데이터를 정규화를 한 뒤, 이후 작업을 실시해야 할 듯
    엑셀에서 Norm.Dist함수를 통하여 각 값을 누적분포함수값으로 정규화를 시킨다(0<=x<=1)
    
    테스트 해보니, 확실히 전보다 유사한 값들이 나오긴 하지만, 크게 막 달라지고 그런건 없었음
    
17/03/03
    spark.ml 패키지의 경우, DataFrame을 지원하는 패키지들,
    애초에 학습할때도 LabeledPoint가 아닌 DataFrame으로 학습시킨다.
    
    scala로 넘어와서 df로 작업하기
    DataFrame로 만드는 작업 완료
    Vector와 ml.Vector와는 다름
    
    2월부터 12월에 해당하는 data 만들기
    각 월별로 만들다 생각난건
        차피 해당 월에 대한 표준편차, 정규분포를 따르기 때문에, 전체 데이터에 대한 작업을 하지 않으면, 크게 의미가 없다고 판단됨
    전체 데이터에 대한 작업 한번 실시해보기
    
    subway의 경우 전 데이터에 대해 부분합을 먼저 구해야 할듯
    
    scala가 유독 느림, pyspark의 경우 상당히 빠름
    
    project 임시 종료 => 현실적인 이유 때문에 종료
        memory 부족으로 인한 pyspark connection 중단사태 잦은 발생
        리소스 부족으로 인한 데이터 학습의 부재 발생
    
    이 프로젝트를 통해 얻은 것 및 파라미터별에 대해 정리해야할 듯

17/03/05
    local ubuntu로 환경 전환 및 ml 모델에 대한 테스트 시작(dataframe)
    실제 raw_Data에서 역 데이터와 시간데이터를 바이너리 벡터형으로 변환, linearRegression 모델에 학습
    어느정도 정확도가 많이 올라간것으로 판별됨
    이후 evaluate 함수를 구성하여, 각 변수별로 설정할 시, 어떤 값에 따라 optimization되는지 확인할 것

17/03/06
	dinstinct()로 했을때도 중복되는 데이터는, 역명은 같으나 호선이 다른 환승역이었음. 이 데이터를 처리하기 위해서 처음부터 raw_data를 재구성 해야함
	데이터 전면 재배치, 파이썬으로 역(코드)데이터를 최적화하려 했으나, 유니코드 및 subway데이터 내부 숫자가 ""로 싸여있는 것 때문에 엑셀을 사용해야함
	따라서 01~12에 대한 데이터 역(코드) => 역으로 변환하여 모든 데이터 수동 취합 실시
	_/data/16XX_name_subway.csv_
		=IF(RIGHT(LEFT(A2,LEN(A2)-5),1)="역",LEFT(A2,LEN(A2)-5),LEFT(A2,LEN(A2)-5)&"역")
	이후 partial_sum.csv의 코드를 변형하여, 부분합을 먼저 진행후, 스크립트 명령어를 통하여 하나로 합친다(16_all_1d_partial.csv)

	일부 깨진 데이터에 대하여 수정 작업을 거친다
	weather역시 월별로 합치도록 한다

	weather 및 subway관련, geocoding code 다시 실행하기
		p9_data_05_location_weather.csv
		p9_data_06_location_subway.csv
		p9_data_07_represent_data.csv

	weather 및 oil_Data merge 실시
	=VLOOKUP($C2,oil_price!$A$2:$E$367,COLUMN(oil_price!B1)-COLUMN(oil_price!$A$1)+1,0)

	12월까지의 데이터가 모두 담기게 되면서, 파일의 규모가 매우 커짐(대략 레코드만 해도, 76만여개)
	파일의 규모가 커지면서 MEM이 8GB인 컴퓨터도 버거워 하는 지경에 다다름
		-> 일단 정규화 과정만 한 파일에 통합하여 진행
	이후 data를 각 분기(quarter)별로 나누어 저장 => 각각 20MB의 파일 크기를 가지게 됨
	열 구성 (역명, 날짜, 시간, s온도, s강수량, s풍속, s고급휘발유, s보통휘발유, s자동차용경유, s실내등유, 인원
		역명, 시간 : 카테코리 특징(DecisionTree) => 바이너리 벡터(LinearModel)
		날짜 : 항목 제외
		s데이터 : 각각 정규화된 데이터로, 특징 벡터를 구성
	
	Git제출 실시  : each of raw_data(quarter) complete




