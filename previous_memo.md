프로젝트 개설한 뒤에,
Scala 설치 https://community.c9.io/t/creating-a-scala-app/1605
Spark 설치를 진행

우분투에 sbt설치시 참조
http://yujuwon.tistory.com/entry/%EC%9A%B0%EB%B6%84%ED%88%AC-1204%EC%97%90-%EC%8A%A4%EC%B9%BC%EB%9D%BC-%EC%84%A4%EC%B9%98%ED%95%98%EA%B8%B0#recentTrackback
http://jdm.kr/blog/90

가상환경 설치 진행하기
sudo pip3 install virtualenv
virtualenv [dir]
source ./[dir]/bin/activate

jupyter설치
sudo pip3 install jupyter

jupyter 실행시
jupyter notebook --ip=0.0.0.0 --port=8080 --no-browser

sbt의 경우

    일단은 해당 디렉터리 들어가서
    sbt
    run
    하면 실행은 됨
    
http://docs.scala-lang.org/ko/tutorials/scala-for-java-programmers.html 여기 참고해서 scala의 기본적인 문법 이해하기
https://twitter.github.io/scala_school/ko/basics.html

mvn은 이미 설치되어 있었음. 기존버전을 지우고 다시 설치해야함
    Cannot find parent: net.java:jvnet-parent for project: com.sun.jersey:jersey-project:pom:1.9 for project com.sun.jersey:jersey-project:pom:1.9
        위와 같은 에러 발생
    해결책 : http://stackoverflow.com/questions/16140389/cannot-find-parent-net-javajvnet-parent-for-project-com-sun-jerseyjersey-pro
    sudo apt-get remove maven2 [To uninstall current maven 2.2.1]
    sudo apt-get install maven [Will install upgraded version] -> 이거 해도 아무 소용 없음
 
    이후 mvn --version으로 확인
    
    http://blog.naver.com/special9486/220234854068 // 여기서 해결책 찾기
    https://opentutorials.org/module/516/5558

32비트인지 64비트인지 확인 가능 - 5가지 방법
    getconf LONG_BIT
    arch
    uname -m
    echo $HOSTYPE
    lscpu | grep ^Arch
    
자바 설치시, 그냥 하면 unauthorized request 에러가 뜨며 다음과 같이 헤더에 쿠키값을 설정해 주어야함
    wget --header "Cookie: oraclelicense=accept-securebackup-cookie" http://download.oracle.com/otn-pub/java/jdk/8u102-b14/jdk-8u102-linux-x64.tar.gz
    다운로드 이후, 이 블로그 참고
    http://blog.naver.com/seilius/220256114356

mvn clean package // 클클린클린클클린클린

보니까 스파크 설치부터 다시, 압축 해제만으로 설치가 끝난게 아니었음
    http://ledgku.tistory.com/71
    환경변수 설정까지 완료

/usr/local : 사용자 설치파일의 위치
    환경변수 설정까지 적용 완료

환경변수 재설정    
http://blog.south10.me/entry/%EC%9A%B0%EB%B6%84%ED%88%AC-%EB%A6%AC%EB%88%85%EC%8A%A4%EC%97%90-%EB%A9%94%EC%9D%B4%EB%B8%90MAVEN-%EC%88%98%EB%8F%99-%EC%84%A4%EC%B9%98%ED%95%98%EA%B8%B0

sbt 사이트에서 설치시 deb패키지 파일을 받은후,
    절차에 맞춰 진행, 에러가 나면 그 패키지를 apt-get install로 설치하면 된다

java version 변경하기
    sbt를 설치하게 되면, 자동적으로 openjdk7까지 동시에 설치되기 때문에 이에대한 기본설정을
    변경하여 설치했던 java8로 변경시켜주어야한다
    http://rockball.tistory.com/entry/Java-Version-%EB%B3%80%EA%B2%BD 블로그 참고!

cloud9 용량 정리하기
    tmp파일 내부 정리 및 ncdu 바이너리 사용
    http://stackoverflow.com/questions/30306455/how-to-keep-control-over-disk-size
    상하키를 이용해서 확인하고, 좌우키로 폴더 내부를 들여다 볼 수 있음
    선택 폴더에서 d를 누르면 삭제 가능
    
    df -h 디스크 용량 확인

maven 인코딩 설정
	<properties>
		<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
		<project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
	</properties>
위와 같은 내용을 pom.xml에 추가해주면 된다.

python app 실행시, 경로 중에 공백이 있게되면 에러를 반출함. 경로중에 있는 공백을 제거할 것!

http://sarc.io/index.php/java/352-about-maven => 자바 에러 해결시 참조
mvn install을 하니까 spark 1.3.1에는 AbstractJavaRDD가 있는반면 2.0.0에는 지원하지 않음
    빠른 포기 ㄱㄱ

spark 2.0.0 자바 문서 : https://spark.apache.org/docs/latest/api/java/overview-summary.html

RDD 개념 : http://www.slideshare.net/sanghoonlee982/spark-overview-20141106?next_slideshow=1

책 원본 페이지 : https://www.packtpub.com/mapt/book/Big-Data-and-Business-Intelligence/9781783288519/1/ch01lvl1sec12/The+first+step+to+a+Spark+program+in+Java

maven 참고 페이지 : http://kjunine.tistory.com/entry/getting-started-with-maven-2

pom.xml에 jdk 버전 빌드 설정하기
    pom.xml 설정 방법 : http://stackoverflow.com/questions/4123044/maven-3-warnings-about-build-plugins-plugin-version
    여기서 버전 확인 : https://maven.apache.org/plugins/index.html
    java 버전은 1.8로 설정함

근데 이상한게 갑자기 돌아감..
중간에 무언가가 설치되서 그런것 같긴한데 ㅠㅠㅠㅠ

참 구동시킬때는

mvn compile 이후
mvn exec:java -Dexec.mainClass="JavaApp" 과 같은 방식으로 실행할 것


3장
가상환경에 matplotlib 설치
sudo pip3 install matplotlib

jup = jupyter notebook --ip=0.0.0.0 --port=8080 --no-browser
jupyter <user profile> --notebook-dir='<user directory>'

ipy -> jup : ipy 다음 jup으로 하면 기본설정으로된 jupyter를 확인할 수 있음

wget http://files.grouplens.org/datasets/movielens/ml-100k.zip 으로 무비렌즈 데이터 집합 다운로드


pylab의 경우 ipython으로 올라오면서 제거된듯함 => matplotlib에 포함됨
    http://stackoverflow.com/questions/31609600/jupyter-ipython-notebook-not-plotting
In에서 %matplotlib inline으로 실행시키면 될듯.
문제는 pyspark


일단 참고
    export SPARK_HOME="$HOME/Downloads/spark-1.4.0-bin-hadoop2.4"
    export IPYTHON=1
    export PYSPARK_PYTHON=/usr/bin/python3
    export PYSPARK_DRIVER_PYTHON=ipython3
    export PYSPARK_DRIVER_PYTHON_OPTS="notebook"


complete// profile에 다음과같은 환경변수 설정후 pyspark로 내용을 실행시킨다(https://www.cloudera.com/documentation/enterprise/5-6-x/topics/spark_ipython.html)
export PYSPARK_PYTHON=/home/ubuntu/workspace/practice/python3/ipy/bin/python3
export PYSPARK_DRIVER_PYTHON=/home/ubuntu/workspace/practice/python3/ipy/bin/jupyter
export PYSPARK_DRIVER_PYTHON_OPTS="notebook --NotebookApp.open_browser=False --NotebookApp.ip='0.0.0.0' --NotebookApp.port=8081"

ipython 단축키
https://gist.github.com/sigmadream/b8b583c8a50c5ea550ea

scipy 설치
http://freeprog.tistory.com/63
    $ sudo apt-get install libblas-dev
    $ sudo apt-get install liblapack-dev
    $ sudo pip install scipy
       ** no fortran compiler 에러시에는 sudo apt-get install gfortran 으로 포트란 설치후 scipy 설치한다.

sudo pip3 list -> 각 파이썬 패키지의 버전 확인 가능

http://makerj.tistory.com/245 모든 파이썬 패키지 업데이트
http://www.bloger.kr/42


ALS.train 실행시
16/10/20 22:44:59 WARN BLAS: Failed to load implementation from: com.github.fommil.netlib.NativeSystemBLAS
16/10/20 22:44:59 WARN BLAS: Failed to load implementation from: com.github.fommil.netlib.NativeRefBLAS
16/10/20 22:45:00 WARN LAPACK: Failed to load implementation from: com.github.fommil.netlib.NativeSystemLAPACK
16/10/20 22:45:00 WARN LAPACK: Failed to load implementation from: com.github.fommil.netlib.NativeRefLAPACK
에러 발생,
https://www.cloudera.com/documentation/enterprise/5-5-x/topics/spark_mllib.html여기서 보면
libfortran3인가를 설치하라고 나옴

https://github.com/fommil/netlib-java 참고
sudo apt-get install libatlas3-base libopenblas-base # 두번째 활성화(libblas.so.3)
sudo apt-get install libgfortran3 
sudo apt-get install libopenblas-devsudo update-alternatives --config libblas.so #첫번째 활성화(libblas.so)
sudo apt-get install libopenblas-dev #첫번째 활성화(libblas.so)
sudo apt-get install libatlas3 //atlas로 활성화
sudo apt-get install liblapack-dev     => 필요 없음

sudo apt-get autoremove

http://danielnouri.org/notes/2012/12/19/libblas-and-liblapack-issues-and-speed,-with-scipy-and-ubuntu/
update-alternatives --get-selections | grep libblas
sudo apt-get install libatlas-dev libatlas-base-dev
    git 말고 이것도 설치하니까 값 설정이 되긴 함
    설정을 했는데 에러는 왜 계속 뜨는걸까...하........
    http://askubuntu.com/questions/626351/uninstalling-atlas-properly/626540    
    
jblas :) /home/ubuntu//.m2/repository/org/jblas -> 패키지가 여기 있어야 로딩이 됨
https://github.com/mikiobraun/jblas
http://jblas.org/
그냥 git 받아서 mvn install하면 됨
 spark-shell --packages org.jblas:jblas:1.2.4-SNAPSHOT
Clear!!!



libatlas-base-dev* libatlas3-base* libopenblas-base* libopenblas-dev* #마지막 두 명령어에서 openblas가 안뜸
liblapack3
sudo update-alternatives --config liblapack.so.3

https://github.com/fommil/netlib-java/blob/master/pom.xml
이거 따라 해보기

netlib 관련
https://github.com/fommil/netlib-java/issues/88
http://stackoverflow.com/questions/37848216/how-to-configure-high-performance-blas-lapack-for-breeze-on-amazon-emr-ec2



내내읿내일붙내일부터
https://github.com/fommil/netlib-java/blob/master/native_system/xbuilds/linux-x86_64/pom.xml
짆짆짆진행

https://books.google.co.kr/books?id=eQjWBQAAQBAJ&pg=PT41&lpg=PT41&dq=/usr/lib/openblas-base/liblapack.so.3&source=bl&ots=5CjXTXAILx&sig=LtoBsyRehBgh3xx3nQ7EUYNZZng&hl=ko&sa=X&ved=0ahUKEwiAls2yuvTPAhXGWLwKHWOLCGkQ6AEIWTAH#v=onepage&q=%2Fusr%2Flib%2Fopenblas-base%2Fliblapack.so.3&f=false
요거 일단 참고

https://developer.ibm.com/linuxonpower/2016/10/13/enable-system-native-blas-library-for-netlib-java-on-openpower-linux-systems-2/


mvn versions:set -DnewVersion=1.0

cd native_system/xbuilds

sudo apt-get install gfortran
mvn versions:set -DnewVersion=1.1 // https://mvnrepository.com/artifact/com.github.fommil.netlib/netlib-native_ref-linux-x86_64/1.1
netlib-java에 있는 pom파일 대로 하다 보면,
내부 파일에 있는 버전이 1.2-SNAPSHOT인걸 볼 수 있다.
x86_64가 1.1버전이 최신이므로, 이에 따라 진행한다


native_ref에서
그냥 mvn install 해버림

mvn deploy -Psonatype-oss-release 이거에서 막힘
gpg 키를 입력하라는데.. 무엇일까

 spark-shell --master yarn --driver-java-options -Dcom.github.fommil.netlib.BLAS=com.github.fommil.netlib.F2jBLAS
-Dcom.github.fommil.netlib.BLAS=com.github.fommil.netlib.NativeRefBLAS


spark-shell -Dcom.github.fommil.netlib.NativeRefLAPACK=com.github.fommil.netlib.LAPACK
    => 안먹힘
.https://github.com/fommil/netlib-java/issues/87

/home/ubuntu/.m2/repository/com/github/fommil/netlib/netlib-native_ref-linux-x86_64/1.1
/home/ubuntu//.m2/repository/com/github/fommil/netlib/netlib-native_system-linux-x86_64/1.1

sudo dpkg -l | grep libgfortran

-Dcom.github.fommil.netlib.NativeRefBLAS=com.github.fommil.netlib.BLAS
-Dcom.github.fommil.netlib.NativeRefLAPACK=com.github.fommil.netlib.LAPACK

포기. 안해먹어


hadoop
    wget http://ftp.daumkakao.com/apache/hadoop/common/hadoop-2.7.3/hadoop-2.7.3.tar.gz
    환경변수 연동
    spark-env.sh
    hadoop-env.sh // JAVA_HOME 경로 잡아주기
    파일 변경   
    http://blog.acronym.co.kr/329
    http://edkoon.tistory.com/410
    standalone 모드일 경우 web ui를 사용할 수 없음
    
    $ export MAVEN_OPTS="-Xmx2g -XX:MaxPermSize=512M -XX:ReservedCodeCacheSize=512m"
    $ mvn -Pyarn -Phadoop-2.7 -Dhadoop.version=2.7.3 -DskipTests clean package
    xmx <size> : JVM의 JAVA heap의 최대(Maximum Size) 크기를 지정한다.
    MaxPermSize로 지정되는 메모리 영역
        xmx와 MaxPermSize는 각개의 영역 -> 추후 합해짐
    -XX:ReservedCodeCacheSize: 컴파일된 메소드의 네이티브 코드 생성을 저장
    
    
    http://blog.naver.com/alice_k106/220436293186 -> standalone 설정
    hdfs-site부터 설정 다시하기
    readonly라 하면서 안됨
    
hadoop
    wget http://hadoop.apache.org/releases.html#25+August%2C+2016%3A+Release+2.7.3+available
    환경변수 연동
    spark-env.sh
    hadoop-env.sh // JAVA_HOME 경로 잡아주기
    파일 변경   
    http://blog.acronym.co.kr/329
    http://edkoon.tistory.com/410
    standalone 모드일 경우 web ui를 사용할 수 없음
    
    $ export MAVEN_OPTS="-Xmx2g -XX:MaxPermSize=512M -XX:ReservedCodeCacheSize=512m"
    $ mvn -Pyarn -Phadoop-2.7 -Dhadoop.version=2.7.3 -DskipTests clean package
    xmx <size> : JVM의 JAVA heap의 최대(Maximum Size) 크기를 지정한다.
    MaxPermSize로 지정되는 메모리 영역
        xmx와 MaxPermSize는 각개의 영역 -> 추후 합해짐
    -XX:ReservedCodeCacheSize: 컴파일된 메소드의 네이티브 코드 생성을 저장
    
        http://andersonjo.github.io/hadoop/2015/09/08/Installing-Hadoop/
        sudo apt-get install ssh rsync
            rsync : 원격에 있는 파일과 디렉토리를 복사하고 동기화 하기 위해서 사용하는 툴이며 동시에 네트워크 프로토콜
            .ssh/authorized_keys: Read-only file system 이런 에러가 발생한다면..
                -> cloud9 account 설정에서 ssh관련 설정이 있음
                -> 이걸 먼저 확인해봐야 할 듯
        ssh 문제를 어떻게 해결할까..
            -> 해결 불능. c9에서는 premium에서만 지원하는것으로 알려짐
    
    
    https://oweissbarth.de/running-sparkmlib-with-native-lapack-and-blas/
    요거 참고해서 뭐라도 할 수 있지 않을까?
    
    mvn package를 하면 jar 파일이 생성됨 -> 근데 안생김
    mvn compile 해도 역시 target 디렉토리나 그런게 없고..
    mvn install까지 함..
    
    https://datasciencemadesimpler.wordpress.com/tag/blas/ 이거 따라가면 될 듯 한데
    
    http://apache-spark-user-list.1001560.n3.nabble.com/MLLIB-usage-BLAS-dependency-warning-td18660.html
    

import com.github.fommil.netlib.BLAS;
System.out.println(BLAS.getInstance().getClass().getName());
jar에서 so를 추출 후
ldd를 했어도 별 효과가 없었음
보니까 NativeRefBLAS를 로딩을 못하면 F2jBLAS로 대체하여 사용하는듯

sudo apt-get install gfortran

일단 최신버전의 OpenBLAS 설치함..
http://m.blog.naver.com/dusrb2003/220550011446 일단 여기랑 동일하게 설치함



16/11/08
http://qiita.com/adachij2002/items/b9af506d704434f4f293(진행중..)
각 pom 파일마다 버전에 맞게 설정 (NativeRef, NativeSystem)1.1이거나 (Others)1.1.2이거나 다양하게
mvn package 4개 진행한 상황
단, system의 x86_64에서만 에러가 발생

161110
SPARK source version으로 다운받은 이후(git clone git://github.com/apache/spark.git)
    build/mvn -Pnetlib-lgpl -DskipTests clean package 
    -> 이거 하니까 메모리가 초과되서 killed당함
        일단은 그냥 f2j쓰다가 나중에 좋은 머신으로 갈아타는게 맞을듯 함
            -> spark 홈페이지에서도 default spark에서는 netlib을 지원 안한다고 명시
                http://spark.apache.org/docs/latest/ml-guide.html
                그나마 마지막까지 따라간거
                    http://apache-spark-user-list.1001560.n3.nabble.com/Mllib-native-netlib-java-OpenBLAS-td19662.html
                
    cloud9에서 설치를 진행하려 했으나,
    zinc에러등 다양한 에러가 또 발생
    포기하기로!


mvn package 이후
jblas .jar파일을 CLASSPATH내부에 둠 -> 바로 인식됨!

sudo apt-get install libatlas3-base libopenblas-base

netlib받은 후에

dversion으로 버전 변경 이후,
    xbuild로 들어가서 mvn package를 하면 에러 발생(물론, 버전 내부 다 1.1로 변경)
    mvn package로 jar파일 생성
    sudo apt-get install gfortran
    이후 다시 mvn package
    sudo apt-get install libopenblas-dev -> 첫번째 openblas로 활성화 -> 이건 처음!
    sudo apt-get install liblapack-dev -> 세번째 활성화
        1,2번째의 경우 openblas로 활성화
        3,4번째의 경우 libblias.so로 활성화
    
    netlib-native_system-linux-x86_64-natives.jar
    netlib-native_ref-linux-x86_64-natives.jar
    netlib-native_system-linux-x86_64-natives.so
    netlib-native_ref-linux-x86_64-natives.so
        - 이렇게 4개의 파일이 생성되었는데..
        처음처럼 jar 2개의파일을 java의 CLASS_PATH에 집어넣는다면? 인식 못함
        source jar 파일을 집어 넣는다면? 
    install 한번 진행할까? 설치는 된것 같음 -> 설치완료
        과연 실행이 될지..? -> 인식 못함
    
    
        jblas 설치에 의의를 두어야 할듯 나머지 인식은 못하니까..
        https://developer.ibm.com/linuxonpower/2016/10/13/enable-system-native-blas-library-for-netlib-java-on-openpower-linux-systems-2/ 이걸 해야하나
        포기합시당
        jblas 설치한걸로 만족!!
        
        마지막으로 source랑 native 모두 java로 옮기긴 해
        
http://yujuwon.tistory.com/entry/%EC%9A%B0%EB%B6%84%ED%88%AC-1204%EC%97%90-%EC%8A%A4%EC%B9%BC%EB%9D%BC-%EC%84%A4%EC%B9%98%ED%95%98%EA%B8%B0
    -> 나중에 ipy에서 scala 사용시 적용시킬 것
        

현재 2.11, 2.12버전 scala 둘 다 설치함(실상 /usr/local에 있는 scala slink만 변경해주면 됨

host에 있던 jupyter 모두 삭제 후, 가상환경에 재설치, 및 scala211 인식시킴


Chapter_09 에러가 아님 http://mail-archives.us.apache.org/mod_mbox/spark-user/201407.mbox/%3CCAJgQjQ_87xkpACXFpoOn0iuLZ2Q00qwWTLHmZfy9BAhKqDkv0A@mail.gmail.com%3E
    Stage 192 contains a task of very large size (9033 KB). The maximum recommended task size is 100 KB.

http://regexr.com/ 정규표현식 테스트 사이트

http://blog.naver.com/lestat85/150184756668 // list와 seq의 차이
    list는 값 변경이 되지 않음
    Array는 값 변경이 가능
    튜플은 _._ 형태로, 나머지는 ()형태로 참조 가능
    
    
https://blog.outsider.ne.kr/565 // sbt 관련

sbt libraryDependencies
    https://mvnrepository.com/artifact/org.apache.spark/spark-streaming_2.11/2.1.0 에서
    
일단 sbt 관련해서 조금 알아보고 10장 시작해야할듯
빌드하는 법도 모르면서 무슨 코드를 짜겠다고..


sbt 재설치 시작
빈 폴더에서 sbt하면 프로젝트 생성하겠냐는 문구가 뜨지를 않음
Duplicate sources.list entry https://dl.bintray.com/sbt/debian/  Packages (/var/lib/apt/lists/dl.bintray.com_sbt_debian_Packages.gz)
    sudo vi /etc/apt/sources.list.d/sbt.list  -> 중복되는 결과를 지우면 됨
    
    프로젝트 생성 문구가, 그냥 임의의 shell 파일 설정 같은데?
        -> shell 코드 작성해서 실행시켜보기로

    프로젝트를 생성해서 sbt에 맞는 디렉터리대로 이행하지 않으면, 에러가 남
        현재, 잘 동작하는 이유는 디렉터리를 만들어주는 shell 파일 설정을 하고 파ㅣㄹ을
            Input 했기 때문
    

    Maven Repository에서 sbt libraryDependencies 잘 찾아 설정해야할 듯
    libraryDependencies += "org.apache.spark" % "spark-mllib_2.11" % "2.1.0"
    libraryDependencies += "org.apache.spark" % "spark-streaming_2.11" % "2.1.0"
    설정 완료
    
    INFO, WARN에 따른 log properties 설정
    보니까 spark-shell과 같은 기본 spark version 2.0.2
            sbt run으로 실행하는 app의 경우 spark version 2.1.0
    버전상 차이가 있음(현재 spark가 2가지가 운용되고 있으니)
    
    따라서 2.1.0에 있는 log properties를 수정하면 WARN으로 동작가능할 듯
    근데 2.1.0이 12.28에 나온 최신버전.. 설정 어떻게하지 이거;;
        설치 안된것 같은데 실행이 된다..?
    
        => 프로젝트 종속적이기 때문에 https://www.safaribooksonline.com/library/view/scala-cookbook/9781449340292/ch18s14.html
            build.sbt 에서 설정을 하거나, 콘솔에서 set으로 설정을하면 된다
            Level.Warn 임 Warning이 아니라,
            하지만 먹히지 않음
        아마 spark 설정이 따로 필요할 듯 하긴 한데
    추후 논의해야할 듯
    
    여하튼 spark 2.1.0도 다운받아서 기본으로 사용 시작
    /usr/local/spark로 symbolic link 설정
