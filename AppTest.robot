*** Settings ***
Library           Collections
Library           RequestsLibrary
Test Timeout      30 seconds

*** Test Cases ***
addActorPass
    Create Session    localhost    http://localhost:8080
    ${headers}=    Create Dictionary    Content-Type=application/json
    ${params}=    Create Dictionary     name=Denzel Washington    actorId=nm1001213
    ${resp}=    Put Request    localhost    /api/v1/addActor    data=${params}    headers=${headers}
    Should Be Equal As Strings    ${resp.status_code}    200

addActorPass
    Create Session    localhost    http://localhost:8080
    ${headers}=    Create Dictionary    Content-Type=application/json
    ${params}=    Create Dictionary     name=Tom Cruise    actorId=nm1001214
    ${resp}=    Put Request    localhost    /api/v1/addActor    data=${params}    headers=${headers}
    Should Be Equal As Strings    ${resp.status_code}    200

addActorPass
    Create Session    localhost    http://localhost:8080
    ${headers}=    Create Dictionary    Content-Type=application/json
    ${params}=    Create Dictionary     name=Kevin Bacon    actorId=nm0000102
    ${resp}=    Put Request    localhost    /api/v1/addActor    data=${params}    headers=${headers}
    Should Be Equal As Strings    ${resp.status_code}    200

addActorPass
    Create Session    localhost    http://localhost:8080
    ${headers}=    Create Dictionary    Content-Type=application/json
    ${params}=    Create Dictionary     name=Al Pacino    actorId=nm1001209
    ${resp}=    Put Request    localhost    /api/v1/addActor    data=${params}    headers=${headers}
    Should Be Equal As Strings    ${resp.status_code}    200

addActorPass
    Create Session    localhost    http://localhost:8080
    ${headers}=    Create Dictionary    Content-Type=application/json
    ${params}=    Create Dictionary     name=Noah Wyle    actorId=nm1001311
    ${resp}=    Put Request    localhost    /api/v1/addActor    data=${params}    headers=${headers}
    Should Be Equal As Strings    ${resp.status_code}    200

addActorPass
    Create Session    localhost    http://localhost:8080
    ${headers}=    Create Dictionary    Content-Type=application/json
    ${params}=    Create Dictionary     name=Charlie Theron    actorId=nm1001320
    ${resp}=   Put Request    localhost    /api/v1/addActor    data=${params}    headers=${headers}
    Should Be Equal As Strings    ${resp.status_code}    200
    
addActorFail
    Create Session    localhost    http://localhost:8080
    ${headers}=    Create Dictionary    Content-Type=application/json
    ${params}=    Create Dictionary     name=Charlie Theron    actorId=nm1001320
    ${resp}=   Put Request    localhost    /api/v1/addActor    data=${params}    headers=${headers}
    Should Be Equal As Strings    ${resp.status_code}    400

addMoviePass
    Create Session    localhost    http://localhost:8080
    ${headers}=    Create Dictionary    Content-Type=application/json
    ${params}=    Create Dictionary     name=Parasite    movieId=nm1001214
    ${resp}=    Put Request    localhost    /api/v1/addMovie   data=${params}    headers=${headers}
    Should Be Equal As Strings    ${resp.status_code}    200
    
addMoviePass
    Create Session    localhost    http://localhost:8080
    ${headers}=    Create Dictionary    Content-Type=application/json
    ${params}=    Create Dictionary     name=A Few Good Men    movieId=nm1012345
    ${resp}=    Put Request    localhost    /api/v1/addMovie   data=${params}    headers=${headers}
    Should Be Equal As Strings    ${resp.status_code}    200

addMoviePass
    Create Session    localhost    http://localhost:8080
    ${headers}=    Create Dictionary    Content-Type=application/json
    ${params}=    Create Dictionary     name=The Matrix    movieId=nm1011214
    ${resp}=    Put Request    localhost    /api/v1/addMovie    data=${params}    headers=${headers}
    Should Be Equal As Strings    ${resp.status_code}    200

addMovieFail
    Create Session    localhost    http://localhost:8080
    ${headers}=    Create Dictionary    Content-Type=application/json
    ${params}=    Create Dictionary     name=The Matrix    movieId=nm1011214
    ${resp}=    Put Request    localhost    /api/v1/addMovie   data=${params}    headers=${headers}
    Should Be Equal As Strings    ${resp.status_code}    400

addRelationshipPass
    Create Session    localhost    http://localhost:8080
    ${headers}=    Create Dictionary    Content-Type=application/json
    ${params}=    Create Dictionary     actorId=nm1001213    movieId=nm1001214
    ${resp}=    Put Request    localhost    /api/v1/addRelationship   data=${params}    headers=${headers}
    Should Be Equal As Strings    ${resp.status_code}    200
    
addRelationshipPass
    Create Session    localhost    http://localhost:8080
    ${headers}=    Create Dictionary    Content-Type=application/json
    ${params}=    Create Dictionary     actorId=nm1001214    movieId=nm1001214
    ${resp}=    Put Request    localhost    /api/v1/addRelationship   data=${params}    headers=${headers}
    Should Be Equal As Strings    ${resp.status_code}    200
    
addRelationshipPass
    Create Session    localhost    http://localhost:8080
    ${headers}=    Create Dictionary    Content-Type=application/json
    ${params}=    Create Dictionary     actorId=nm0000102    movieId=nm1011214
    ${resp}=    Put Request    localhost    /api/v1/addRelationship   data=${params}    headers=${headers}
    Should Be Equal As Strings    ${resp.status_code}    200

addRelationshipPass
    Create Session    localhost    http://localhost:8080
    ${headers}=    Create Dictionary    Content-Type=application/json
    ${params}=    Create Dictionary     actorId=nm1001209    movieId=nm1011214
    ${resp}=    Put Request    localhost    /api/v1/addRelationship   data=${params}    headers=${headers}
    Should Be Equal As Strings    ${resp.status_code}    200

addRelationshipPass
    Create Session    localhost    http://localhost:8080
    ${headers}=    Create Dictionary    Content-Type=application/json
    ${params}=    Create Dictionary     actorId=nm1001311    movieId=nm1011214
    ${resp}=    Put Request    localhost    /api/v1/addRelationship   data=${params}    headers=${headers}
    Should Be Equal As Strings    ${resp.status_code}    200
    
addRelationshipPass
    Create Session    localhost    http://localhost:8080
    ${headers}=    Create Dictionary    Content-Type=application/json
    ${params}=    Create Dictionary     actorId=nm1001320    movieId=nm1012345
    ${resp}=    Put Request    localhost    /api/v1/addRelationship   data=${params}    headers=${headers}
    Should Be Equal As Strings    ${resp.status_code}    200
    
addRelationshipPass
    Create Session    localhost    http://localhost:8080
    ${headers}=    Create Dictionary    Content-Type=application/json
    ${params}=    Create Dictionary     actorId=nm1001320    movieId=nm1011214
    ${resp}=    Put Request    localhost    /api/v1/addRelationship   data=${params}    headers=${headers}
    Should Be Equal As Strings    ${resp.status_code}    200
    
addRelationshipPass
    Create Session    localhost    http://localhost:8080
    ${headers}=    Create Dictionary    Content-Type=application/json
    ${params}=    Create Dictionary     actorId=nm1001320    movieId=nm1001214
    ${resp}=    Put Request    localhost    /api/v1/addRelationship   data=${params}    headers=${headers}
    Should Be Equal As Strings    ${resp.status_code}    200
    
addRelationshipFail
    Create Session    localhost    http://localhost:8080
    ${headers}=    Create Dictionary    Content-Type=application/json
    ${params}=    Create Dictionary     actorId=nm1001213    movieId=nm123456789123456
    ${resp}=    Put Request    localhost    /api/v1/addRelationship   data=${params}    headers=${headers}
    Should Be Equal As Strings    ${resp.status_code}    404

addMovieYearPass
	Create Session    localhost    http://localhost:8080
    ${headers}=    Create Dictionary    Content-Type=application/json
    ${params}=    Create Dictionary     year=${2011}    movieId=nm1001214
    ${resp}=    Put Request    localhost    /api/v1/addMovieYear    data=${params}    headers=${headers}
    Should Be Equal As Strings    ${resp.status_code}    200
    
addMovieYearFail
	Create Session    localhost    http://localhost:8080
    ${headers}=    Create Dictionary    Content-Type=application/json
    ${params}=    Create Dictionary     year=${2011}    movieId=nm123456789123456
    ${resp}=    Put Request    localhost    /api/v1/addMovieYear    data=${params}    headers=${headers}
    Should Be Equal As Strings    ${resp.status_code}    404
    
addIMDbRatingPass
	Create Session    localhost    http://localhost:8080
    ${headers}=    Create Dictionary    Content-Type=application/json
    ${params}=    Create Dictionary     IMDbRating=${7}    movieId=nm1001214
    ${resp}=    Put Request    localhost    /api/v1/addIMDbRating    data=${params}    headers=${headers}
    Should Be Equal As Strings    ${resp.status_code}    200
	
addIMDbRatingFail
	Create Session    localhost    http://localhost:8080
    ${headers}=    Create Dictionary    Content-Type=application/json
    ${params}=    Create Dictionary     IMDbRating=${7}    movieId=nm123456789123456
    ${resp}=    Put Request    localhost    /api/v1/addIMDbRating    data=${params}    headers=${headers}
    Should Be Equal As Strings    ${resp.status_code}    404

addMPAARatingPass
	Create Session    localhost    http://localhost:8080
    ${headers}=    Create Dictionary    Content-Type=application/json
    ${params}=    Create Dictionary     MPAARating=G    movieId=nm1001214
    ${resp}=    Put Request    localhost    /api/v1/addMPAARating    data=${params}    headers=${headers}
    Should Be Equal As Strings    ${resp.status_code}    200
    
addMPAARatingFail
	Create Session    localhost    http://localhost:8080
    ${headers}=    Create Dictionary    Content-Type=application/json
    ${params}=    Create Dictionary     MPAARating=G    movieId=nm123456789123456
    ${resp}=    Put Request    localhost    /api/v1/addMPAARating    data=${params}    headers=${headers}
    Should Be Equal As Strings    ${resp.status_code}    404

getActorPass
    Create Session    localhost    http://localhost:8080
    ${headers}=    Create Dictionary    Content-Type=application/json
    ${params}=    Create Dictionary     actorId=nm1001320    
    ${resp}=    Put Request    localhost    /api/v1/getActor  data=${params}    headers=${headers}
    ${movieList}=  Create List     nm1001214   nm1011214   nm1012345
    ${json_pass}=   Create Dictionary    actorId=nm1001320    name=Charlie Theron   movies=${movieList}
    Should Be Equal As Strings    ${resp.status_code}    200
    Dictionaries Should Be Equal    ${resp.json()}  ${json_pass}

getActorFail
    Create Session    localhost    http://localhost:8080
    ${headers}=    Create Dictionary    Content-Type=application/json
    ${params}=    Create Dictionary     actorId=nm123456789123456   
    ${resp}=    Put Request    localhost    /api/v1/getActor  data=${params}    headers=${headers}
    Should Be Equal As Strings    ${resp.status_code}    404

getMoviePass
    Create Session    localhost    http://localhost:8080
    ${headers}=    Create Dictionary    Content-Type=application/json
    ${params}=    Create Dictionary     movieId=nm1011214
    ${actorsList}=  Create List    nm1001320    nm1001311   nm1001209   nm0000102
    ${json_pass}=   Create Dictionary   movieId=nm1011214    name=The Matrix   actors=${actorsList}
    ${resp}=    Put Request    localhost    /api/v1/getMovie  data=${params}    headers=${headers}
    Should Be Equal As Strings    ${resp.status_code}    200
    Dictionaries Should Be Equal    ${resp.json()}  ${json_pass}

getMovieFail
    Create Session    localhost    http://localhost:8080
    ${headers}=    Create Dictionary    Content-Type=application/json
    ${params}=    Create Dictionary     movieId=nm123456789123456    
    ${resp}=    Put Request    localhost    /api/v1/getMovie  data=${params}    headers=${headers}
    Should Be Equal As Strings    ${resp.status_code}    404

hasRelationshipPass
    Create Session    localhost    http://localhost:8080
    ${headers}=    Create Dictionary    Content-Type=application/json
    ${params}=    Create Dictionary     actorId=nm1001213    movieId=nm1001214
    ${json_pass}=   Create Dictionary   actorId=nm1001213    movieId=nm1001214  hasRelationship=${true}     
    ${resp}=    Put Request    localhost    /api/v1/hasRelationship  data=${params}    headers=${headers}
    Should Be Equal As Strings    ${resp.status_code}    200
    Dictionaries Should Be Equal    ${resp.json()}  ${json_pass}

hasRelationshipFail
    Create Session    localhost    http://localhost:8080
    ${headers}=    Create Dictionary    Content-Type=application/json
    ${params}=    Create Dictionary     actorId=nm1001213    movieId=nm123456789123456     
    ${resp}=    Put Request    localhost    /api/v1/hasRelationship  data=${params}    headers=${headers}
    Should Be Equal As Strings    ${resp.status_code}    404

computeBaconNumberPass
    Create Session    localhost    http://localhost:8080
    ${headers}=    Create Dictionary    Content-Type=application/json
    ${params}=    Create Dictionary     actorId=nm1001213
    ${json_pass}=   Create Dictionary   baconNumber=${4}  
    ${resp}=    Put Request    localhost    /api/v1/computeBaconNumber  data=${params}    headers=${headers}
    Should Be Equal As Strings    ${resp.status_code}    200
    Dictionaries Should Be Equal    ${resp.json()}  ${json_pass}

computeBaconNumberFail
    Create Session    localhost    http://localhost:8080
    ${headers}=    Create Dictionary    Content-Type=application/json
    ${params}=    Create Dictionary     actorId=nm123456789123456   
    ${resp}=    Put Request    localhost    /api/v1/computeBaconNumber  data=${params}    headers=${headers}
    Should Be Equal As Strings    ${resp.status_code}    404

computeBaconPathPass
    Create Session    localhost    http://localhost:8080
    ${headers}=    Create Dictionary    Content-Type=application/json
    ${params}=    Create Dictionary     actorId=nm1001213
    ${baconPathList}=   Create List     nm1001213   nm1001214   nm1001320   nm1011214   nm0000102
    ${json_pass}=   Create Dictionary   baconPath=${baconPathList}
    ${resp}=    Put Request    localhost    /api/v1/computeBaconPath  data=${params}    headers=${headers}
    Should Be Equal As Strings    ${resp.status_code}    200
    Dictionaries Should Be Equal    ${resp.json()}  ${json_pass}

computeBaconPathFail
    Create Session    localhost    http://localhost:8080
    ${headers}=    Create Dictionary    Content-Type=application/json
    ${params}=    Create Dictionary     actorId=nm123456789123456   
    ${resp}=    Put Request    localhost    /api/v1/computeBaconPath  data=${params}    headers=${headers}
    Should Be Equal As Strings    ${resp.status_code}    404
    
getMovieYearPass
	Create Session    localhost    http://localhost:8080
    ${headers}=    Create Dictionary    Content-Type=application/json
    ${params}=    Create Dictionary     year=${2011}
    ${movieList}=   Create List        nm1001214
    ${json_pass}=   Create Dictionary   year=${2011}    movies=${movieList}
    ${resp}=    Put Request    localhost    /api/v1/getMovieYear  data=${params}    headers=${headers}
    Should Be Equal As Strings    ${resp.status_code}    200
    Dictionaries Should Be Equal    ${resp.json()}  ${json_pass}
	
getMovieYearFail
	Create Session    localhost    http://localhost:8080
    ${headers}=    Create Dictionary    Content-Type=application/json
    ${params}=    Create Dictionary     year=${123}   
    ${resp}=    Put Request    localhost    /api/v1/getMovieYear  data=${params}    headers=${headers}
    Should Be Equal As Strings    ${resp.status_code}    404
    
getIMDbRatingPass
	Create Session    localhost    http://localhost:8080
    ${headers}=    Create Dictionary    Content-Type=application/json
    ${params}=    Create Dictionary     IMDbRating=${7}
    ${movieList}=   Create List        nm1001214
    ${json_pass}=   Create Dictionary   IMDbRating=${7}    movies=${movieList}   
    ${resp}=    Put Request    localhost    /api/v1/getIMDbRating  data=${params}    headers=${headers}
    Should Be Equal As Strings    ${resp.status_code}    200
    Dictionaries Should Be Equal    ${resp.json()}  ${json_pass}
    
getIMDbRatingFail
	Create Session    localhost    http://localhost:8080
    ${headers}=    Create Dictionary    Content-Type=application/json
    ${params}=    Create Dictionary     IMDbRating=${100}   
    ${resp}=    Put Request    localhost    /api/v1/getIMDbRating  data=${params}    headers=${headers}
    Should Be Equal As Strings    ${resp.status_code}    400
    
getMPAARatingPass
	Create Session    localhost    http://localhost:8080
    ${headers}=    Create Dictionary    Content-Type=application/json
    ${params}=    Create Dictionary     MPAARating=G
    ${movieList}=   Create List        nm1001214
    ${json_pass}=   Create Dictionary   MPAARating=G    movies=${movieList}       
    ${resp}=    Put Request    localhost    /api/v1/getMPAARating  data=${params}    headers=${headers}
    Should Be Equal As Strings    ${resp.status_code}    200
    Dictionaries Should Be Equal    ${resp.json()}  ${json_pass}

getMPAARatingFail
	Create Session    localhost    http://localhost:8080
    ${headers}=    Create Dictionary    Content-Type=application/json
    ${params}=    Create Dictionary     MPAARating=123456   
    ${resp}=    Put Request    localhost    /api/v1/getMPAARating  data=${params}    headers=${headers}
    Should Be Equal As Strings    ${resp.status_code}    404