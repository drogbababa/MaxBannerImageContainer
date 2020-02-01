MaxBannerImageContainer
=====

MaxBannerImageContainer is a custom viewgroup that is animated the commonly hero display view used in Max + App. The effect is as follows:

(MaxBannerImageContainer是仿照Max+App中常用英雄展示控件制作的一个控件。效果如下:)

![](/show.gif)

## Change Log

- v1.1
 
  MaxBannerImageContainer handles showing and hiding item views itself, including touch event and animations.

- v2.0

  MaxBannerImageContainer uses a ViewPager to contains item views, including handling touch event and flip animations.

## How to use:

(使用方式：)

1. Add it in your root build.gradle at the end of repositories:

    ``` xml
    allprojects {
        repositories {
            ...
            maven { url 'https://jitpack.io' }
        }
    }
    ```
    	
2. Add the dependency

    ```implementation 'com.github.drogbababa:MaxBannerImageContainer:v2.0'```

3. In Activity.xml

    ``` xml
    <com.drogbababa.maxbannerimagecontainer.MaxBannerImageContainer
            android:id="@+id/max_banner_view"
            android:layout_width="300dp"
            android:layout_height="200dp"
            android:layout_marginLeft="20dp"
            app:radius="@dimen/radius11">
     </com.drogbababa.maxbannerimagecontainer.MaxBannerImageContainer>
     ```

4. In Java file
    - Directly use or extends MaxBaseItem to save data. [getViewPagerDatas()](sample/src/main/java/com/drogbababa/maxbannerimagecontainer/MainActivity.java)
    - set List<MaxBaseItem> to MaxBannerImageContainer. [onCreate()](sample/src/main/java/com/drogbababa/maxbannerimagecontainer/MainActivity.java)
    
    ```
       MaxBannerImageContainer maxBannerImageContainer = findViewById(R.id.max_banner_view);
       maxBannerImageContainer.setData(getViewPagerDatas());
       maxBannerImageContainer.setOnItemClickListener(new MaxBannerImageContainer.OnItemClickListener() {
           @Override
           public void onItemClick(View view, int position) {
               Toast.makeText(MainActivity.this, "position " + position, Toast.LENGTH_SHORT).show();
           }
       });
    ```
    
Any bugs in use are welcome to issue~