/*
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:app="http://schemas.android.com/apk/res-auto"
        xmlns:segmentedgroup="http://schemas.android.com/apk/res-auto"
        xmlns:tools="http://schemas.android.com/tools"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:background="@color/white"
        tools:context="apsupportapp.aperotechnologies.com.designapp.ProductInformation.StyleActivity">


<RelativeLayout
android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        android:layout_alignParentTop="true"
        android:layout_alignParentStart="true">


<LinearLayout
android:id="@+id/linear_toolbar"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical">

<android.support.v7.widget.Toolbar
        android:id="@+id/toolbar"
        android:layout_width="match_parent"
        android:layout_height="@dimen/toolbar_header_height"
        android:layout_alignParentTop="true"
        android:background="@color/white"
        android:contentInsetEnd="0dp"
        android:contentInsetLeft="0dp"
        android:contentInsetRight="0dp"
        android:contentInsetStart="0dp"
        segmentedgroup:contentInsetEnd="0dp"
        segmentedgroup:contentInsetLeft="0dp"
        segmentedgroup:contentInsetRight="0dp"
        segmentedgroup:contentInsetStart="0dp">

<RelativeLayout
android:layout_width="match_parent"
        android:layout_height="match_parent">

<RelativeLayout
android:id="@+id/rel_ez_back"
        android:layout_width="@dimen/header_clickable_width"
        android:layout_height="@dimen/header_clickable_height"
        android:layout_alignParentLeft="true"
        android:layout_centerVertical="true"
        android:layout_marginLeft="0dp"
        android:background="@drawable/button_click">

<Button
android:layout_width="@dimen/arrow_back_icon_width"
        android:layout_height="@dimen/arrow_back_icon_height"
        android:layout_centerInParent="true"
        android:background="@mipmap/ic_arrow_back_black"
        android:clickable="false" />

</RelativeLayout>

<TextView
android:id="@+id/toolbar_title"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_centerVertical="true"
        android:layout_marginLeft="20dp"
        android:layout_toRightOf="@+id/rel_ez_back"
        android:text="@string/pva_title"
        android:textColor="#000000"
        android:textSize="20sp" />

<RelativeLayout
android:layout_toLeftOf="@+id/rel_ez_sort"
        android:id="@+id/rel_ez_filter"
        android:layout_width="@dimen/header_clickable_width"
        android:layout_height="@dimen/header_clickable_height"
        android:layout_centerVertical="true"
        android:layout_marginRight="0dp"
        android:background="@drawable/button_click">

<Button
android:layout_width="@dimen/icon_width"
        android:layout_height="@dimen/icon_height"
        android:layout_centerInParent="true"
        android:background="@mipmap/iconfilter"
        android:clickable="false" />

</RelativeLayout>
<RelativeLayout
android:id="@+id/rel_ez_sort"
        android:layout_width="@dimen/header_clickable_width"
        android:layout_height="@dimen/header_clickable_height"
        android:layout_alignParentRight="true"
        android:layout_centerVertical="true"
        android:layout_marginRight="0dp"
        android:background="@drawable/button_click">

<Button
android:layout_width="@dimen/icon_width"
        android:layout_height="@dimen/icon_height"
        android:layout_centerInParent="true"
        android:background="@mipmap/iconmore"
        android:clickable="false" />

</RelativeLayout>

</RelativeLayout>
</android.support.v7.widget.Toolbar>



</LinearLayout>


<android.support.design.widget.TabLayout
        android:id="@+id/tabview_ez_salespva"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_below="@+id/linear_toolbar"
        segmentedgroup:tabIndicatorColor="@color/ezfb_Red"
        segmentedgroup:tabSelectedTextColor="@color/ezfb_Red"
        segmentedgroup:tabTextAppearance="@android:style/TextAppearance.Widget.TabWidget"
        segmentedgroup:tabTextColor="@color/ezfb_Red"></android.support.design.widget.TabLayout>

<!--<android.support.v4.widget.NestedScrollView-->
<!--android:scrollbars="vertical"-->
<!--android:layout_width="match_parent"-->
<!--android:layout_height="wrap_content"-->
<!--&gt;-->

<RelativeLayout
android:background="@color/white"
        android:layout_below="@+id/tabview_ez_salespva"
        android:id="@+id/llayoutEzSalesPvA"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical">

<!--<com.numetriclabz.numandroidcharts.GaugeChart-->
<!--android:layout_marginTop="10dp"-->
<!--android:id="@+id/gauge_chart"-->
<!--android:layout_width="match_parent"-->
<!--android:layout_height="270dp" />-->


<RelativeLayout
android:orientation="vertical"
        android:id="@+id/rel_ez_Chartlayout"
        android:layout_width="match_parent"
        android:layout_height="wrap_content">


<TextView
android:id="@+id/pva_noChart"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_centerInParent="true"
        android:gravity="center"
        android:text="No Chart data available"
        android:textSize="@dimen/largelayout_storesize"
        android:visibility="gone" />

<com.github.mikephil.charting.charts.BarChart
        android:id="@+id/ez_bar_chart"
        android:layout_width="match_parent"
        android:layout_height="200dp"
        android:nestedScrollingEnabled="true" />

<ProgressBar xmlns:android="http://schemas.android.com/apk/res/android"
        android:id="@+id/ez_progressBar"
        android:layout_width="match_parent"
        android:layout_height="70dp"
        android:layout_centerInParent="true"
        android:background="#0000"
        android:keepScreenOn="true"
        android:padding="10dp"
        android:theme="@style/ProgressBar"
        android:visibility="gone">

</ProgressBar>

<TextView
android:layout_width="match_parent"
        android:layout_height="20dp"
        android:layout_alignParentRight="true"
        android:layout_below="@+id/ez_bar_chart"
        android:layout_marginRight="15dp"
        android:gravity="right|center_horizontal"
        android:text="@string/xaxis_label"
        android:textColor="@color/ezfbb_black"
        android:textSize="11sp" />
</RelativeLayout>



<LinearLayout
android:id="@+id/llpvahierarchy"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_below="@+id/rel_ez_Chartlayout"
        android:layout_marginTop="8dp"
        android:orientation="horizontal">

<TextView
android:id="@+id/txtpvahDeptName"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_margin="10dp"
        android:textColor="@color/ezfbb_black"
        android:textSize="@dimen/normallayout_textsize" />

</LinearLayout>

<RelativeLayout
android:id="@+id/relTablelayout"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_below="@+id/llpvahierarchy"
        android:layout_marginTop="10dp">

<View
android:id="@+id/view1"
        android:layout_width="match_parent"
        android:layout_height="1dip"
        android:background="@color/smdm_divider_Black" />


<LinearLayout
android:id="@+id/linear"
        android:layout_width="match_parent"
        android:layout_height="80dp"
        android:layout_below="@+id/view1"
        android:orientation="vertical">

<RelativeLayout
android:id="@+id/llheader"
        android:layout_width="match_parent"
        android:layout_height="40dp"
        android:background="@color/ezfb_activity_background">

<RelativeLayout
android:layout_width="match_parent"
        android:layout_height="50dp"
        android:layout_marginRight="10dp"
        android:layout_toLeftOf="@+id/txtPlanSales">

<RelativeLayout
android:id="@+id/btnEzoneBack"
        android:layout_width="20dp"
        android:layout_height="40dp"
        android:layout_centerVertical="true">

<Button
android:layout_width="20dp"
        android:layout_height="20dp"
        android:layout_centerInParent="true"
        android:background="@mipmap/back_arrow"
        android:clickable="false" />
</RelativeLayout>

<TextView
android:id="@+id/txtPlanClass"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_centerInParent="true"
        android:layout_toRightOf="@+id/btnEzoneBack"
        android:text="Department"
        android:textColor="@color/ezfbb_black"
        android:textSize="@dimen/normallayout_textsize"
        android:textStyle="normal" />


<RelativeLayout
android:id="@+id/btnEzoneNext"
        android:layout_width="20dp"
        android:layout_height="40dp"
        android:layout_centerVertical="true"
        android:layout_toRightOf="@+id/txtPlanClass">

<Button
android:layout_width="20dp"
        android:layout_height="20dp"
        android:layout_centerInParent="true"
        android:background="@mipmap/next_arrow"
        android:clickable="false" />
</RelativeLayout>

</RelativeLayout>

<TextView
android:id="@+id/txtPlanSales"
        android:layout_width="70dp"
        android:layout_height="wrap_content"
        android:layout_alignParentRight="true"
        android:layout_centerVertical="true"
        android:layout_gravity="center_vertical"
        android:layout_marginRight="160dp"
        android:layout_toLeftOf="@+id/txtNetSales"
        android:gravity="center_vertical|center"
        android:text="@string/plan_sales"
        android:textColor="@color/ezfbb_black"
        android:textSize="@dimen/normallayout_textsize"
        android:textStyle="normal" />


<TextView
android:id="@+id/txtNetSales"
        android:layout_width="70dp"
        android:layout_height="wrap_content"
        android:layout_alignParentRight="true"
        android:layout_centerVertical="true"
        android:layout_marginRight="90dp"
        android:layout_toLeftOf="@+id/rel"
        android:gravity="center_vertical|center"
        android:text="@string/net_sales"
        android:textColor="@color/ezfbb_black"
        android:textSize="@dimen/normallayout_textsize"
        android:textStyle="normal" />


<TextView
android:id="@+id/rel"
        android:layout_width="90dp"
        android:layout_height="wrap_content"
        android:layout_alignParentRight="true"
        android:layout_centerVertical="true"
        android:gravity="center_vertical|center"
        android:text="Ach%"
        android:textColor="@color/ezfbb_black"
        android:textSize="@dimen/normallayout_textsize"
        android:textStyle="normal" />

</RelativeLayout>

<RelativeLayout
android:layout_width="match_parent"
        android:layout_height="40dp">
<EditText
android:layout_width="match_parent"
        android:layout_height="30dp"
        android:layout_alignParentRight="true"
        android:layout_centerVertical="true"
        android:layout_marginRight="230dp"
        android:layout_toLeftOf="@+id/edt_plansales"
        android:background="@drawable/border"
        android:editable="false"
        android:gravity="center_vertical"
        android:padding="2dp"
        android:textColor="@android:color/black" />


<EditText
android:id="@+id/edt_plansales"
        android:layout_width="70dp"
        android:layout_height="30dp"
        android:layout_alignParentRight="true"
        android:layout_centerVertical="true"
        android:layout_marginRight="160dp"
        android:layout_toLeftOf="@+id/edt_netsales"
        android:background="@drawable/border"
        android:editable="false"
        android:gravity="center_vertical"
        android:paddingRight="2dp" />


<EditText
android:id="@+id/edt_netsales"
        android:layout_width="70dp"
        android:layout_height="30dp"
        android:layout_alignParentRight="true"
        android:layout_centerVertical="true"
        android:layout_marginRight="90dp"
        android:layout_toLeftOf="@+id/edt_pvaSales"
        android:background="@drawable/border"
        android:editable="false"
        android:textColor="@android:color/black" />

<EditText
android:id="@+id/edt_pvaSales"
        android:layout_width="90dp"
        android:layout_height="30dp"
        android:layout_alignParentRight="true"
        android:layout_centerVertical="true"
        android:background="@drawable/border"
        android:editable="false"
        android:textColor="@android:color/black" />


</RelativeLayout>

</LinearLayout>

<android.support.v7.widget.RecyclerView
        android:id="@+id/ez_list"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="45dp"
        android:divider="@null"
        android:dividerHeight="0dp"
        android:smoothScrollbar="true" />

</RelativeLayout>


</RelativeLayout>
<!--</android.support.v4.widget.NestedScrollView>-->
</RelativeLayout>


</RelativeLayout>*/
