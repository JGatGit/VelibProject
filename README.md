# VelibProject
Java student project that simulates a Bike rental system

General Information: The project was part of a “Software Design with UML” class during my Erasmus semester in France. The focus was on the creation of a class concept with UML and the implementation of the basic functions. Therefore it is not optimized for runtime-speed, or a nice user experience, however it is a proof for Java and general programming skills.

The basic idea is:

Given:  (inside "files"-folder)
1. Initial occupation and basic information of bike stations.
2. A recorded list of bike trips of one day.

Task:
1. Simulate how the bike trips of one day affect station occupation.
2. Estimate the ratio of trips that cannot be performed due to empty or fully occupied stations.
3. Also check how simulation conditions affect a potential equilibrium after multiple days.

Taken into account:
1. Bike regulation with trucks (Regulation on/off)
2. Potentially more trips (% Popularity Growth)
3. Users partially adapting to station proposals within a small radius (% User Collaboration)

The code can be accessed easily at the folder “SourceFiles”. The folder "Programm" contains an executeable Version ("Velib.jar"). “Vélib_Documentation.pdf” contains general programm information with UML.  “Velib_Guide_DUtilisation.pdf” is a user manual. After clicking the specific simulation buttons the program takes some time (about 30 seconds) for the simulation and will not respond in the meantime. Since the project was part of my Erasmus Semester, the documentation language is French.
