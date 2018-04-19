# CSYE7200-SPRING2018-TEAM5  <img src="http://d1marr3m5x4iac.cloudfront.net/images/block/I0-001/039/361/539-0.png_/big-data-scala-spark-certification-training-bootc-39.png" alt="alt text" width="128" height="90">
[![CircleCI](https://circleci.com/gh/YichuanZhang/CSYE7200-SPRING2018-TEAM5/tree/master.svg?style=shield)](https://circleci.com/gh/YichuanZhang/CSYE7200-SPRING2018-TEAM5/tree/master)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/3a3e74d5785b4e659780219661deb17f)](https://www.codacy.com/app/YichuanZhang/CSYE7200-SPRING2018-TEAM5?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=YichuanZhang/CSYE7200-SPRING2018-TEAM5&amp;utm_campaign=Badge_Grade)

Project Repository for CSYE7200 Team 5

Team Member: 

Yichuan Zhang - zhang.yichu@husky.neu.edu

Houze Liu - liu.hou@husky.neu.edu
# Fashion Recommender on Amazon <img src="https://forums.developer.amazon.com/spaces/12/icon.html?t=1474581400000" alt="alt text" width="174" height="75">


<a href="https://docs.google.com/presentation/d/1fScP5hZRLKOM23TbkYGZmb-hHZBuYSCI8RtVlUGqGXk/edit?usp=sharing">Planning Presentation</a>

<a href="https://docs.google.com/presentation/d/1ckQuPZqCPWy-GedrfxU6MgB1i0t3cHpngHebOyEmBug/edit?usp=sharing">Final Presentation</a>

# Abstract
Our goal is to process data from responses of calling Amazon Product Advertising API, extact meaningful features and analyze the trend on Amazon especially in clothing and fashion department. We select color, brand and price as our basic features and use them to find their proportions and relationships. We have two use cases: As a customer, you need to provide a keyword that you are interested in and our system tells you most popular colors, brands and how expensive are products on Amazon. That may help you make better shopping choices online. As a seller, you may provide as many keywords as you want, our system allows you build your simple database according to your interests.This may help you make better marketing strategy, including pricing and customer-targeting. Then the project runs Spark on the database to tell you the trend. 

# Methodology
1\.  Preprocessing: Generating Url with Amazon account signature.

2\.  Acquiring: Calling Amazon Product Advertising API.

3\.  Parsing: XML Format into Case Class.

4\.  Feature Extracting: Get “Color”, “Brand”, “Price” and “Item Url” from all items. 

5\.  Mapping & Reducing: Using Apache Spark.

6\.  Visualization: Using Apache Zeppelin.   

# Something about the API
There are a few restrictions on Amazon Product Advertising API if you don't have an Amazon verified advertising account and so do we.

1\. Number of API calls: Limited to 8,640 requests per day. once this limit is reached, will be limited to one request every ten seconds

2\. Number of item pages: Only allow at most 10 pages of items,10 items per page. Usually pages after 6th are blank.

3\. Url timeout: Amazon API plants timestamps in Url which will expire in 15 minutes.

4\. Frequency of API calls: Amazon server may refuse API calls if Amazon thinks request is temporary overload.


# Continuous integration
This project is using CircleCI as the continuous integration tool.

Current Status:
[![CircleCI](https://circleci.com/gh/YichuanZhang/CSYE7200-SPRING2018-TEAM5/tree/master.svg?style=svg)](https://circleci.com/gh/YichuanZhang/CSYE7200-SPRING2018-TEAM5/tree/master)
