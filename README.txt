Site Crawler

Written with Java 1.8 and build with gradle

A simple functional test is written com.github.namuan.SiteCrawlerFunctionalTest.shouldCrawlSite to test

Running the following will setup gradle if it doesn't exist and runs all the tests and prints the output on stdout
$ ./gradlew clean test --info

---

Tradeoffs:
1. It won't handle subdomains during url matching
2. Simple mock class and dependency injection instead of using a framework
3. Single threaded
4. Not all edge cases are unit tested
5. Sitemap output could be improved