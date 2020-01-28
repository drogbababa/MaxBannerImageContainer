MaxBannerImageContainer
=====

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.bumptech.glide/glide/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.bumptech.glide/glide) [![Build Status](https://travis-ci.org/bumptech/glide.svg?branch=master)](https://travis-ci.org/bumptech/glide)
| [View Glide's documentation][20] | [简体中文文档][22] | [Report an issue with Glide][5]

MaxBannerImageContainer is a custom viewgroup that is animated the commonly hero display view used in Max + App. The effect is as follows:
MaxBannerImageContainer是仿照Max+App中常用英雄展示控件制作的一个控件。效果如下:

![](/show.gif)

How to use:
使用方式：
1. Add it in your root build.gradle at the end of repositories:
    	allprojects {
    		repositories {
    			...
    			maven { url 'https://jitpack.io' }
    		}
    	}
2. Add the dependency
    implementation 'com.github.drogbababa:MaxBannerImageContainer:Tag'
    
Any bugs in use are welcome to issue~