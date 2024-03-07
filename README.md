To run this application, we need GSON jar which I have added under /lib folder, you can add this lib by configuring the build path after opening this project in eclipse

If you export this application as Runnable jar, then use the below command to run the application.
java -jar <your-jar-file> --config config.json --betting-amount <some-integer-value>

You can also directly run the application in eclipse if you add this VM arguments
eg:
--config C:\Users\CodingTest\src\config.json --betting-amount 100
