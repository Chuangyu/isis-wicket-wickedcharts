isis-wicket-wickedcharts
========================

An extension for [Apache Isis](http://isis.apache.org)' [Wicket viewer](http://isis.apache.org/components/viewers/wicket/about.html) with [Wicked Charts](https://code.google.com/p/wicked-charts/).  *Wicked Charts* is in turn an integration between [Apache Wicket](http://wicket.apache.org) and the [Highcharts](http://www.highcharts.com/) JS charting library).

Any chart supported by *Wicked Charts* (see their [showcase](http://wicked-charts.appspot.com/) app) should work.

**Please note that while this project and *Wicked Charts* are licensed under Apache 2.0 License, *Highcharts* itself is only free for non-commercial use.  See [here](http://shop.highsoft.com/highcharts.html) for further details.**

### Features

The library provides two separate components/extensions for the Wicket viewer:

* `scalarchart`: renders a standalone scalar value as a chart
* `summarycharts`: render a standalone collection with `BigDecimal` properties as a a chart (one chart per `BigDecimal` property)

The `summarycharts` component can be thought of as an enhancement of the base `summary` view provided by Wicket UI viewer.


## Screenshots

#### Scalar Chart

The following screenshots are based on simple action added to the app generated by Isis' [quickstart archetype](http://isis.apache.org/getting-started/quickstart-archetype.html).  See below for details.

Invoking an action:

![](https://raw.github.com/danhaywood/isis-wicket-wickedcharts/master/images/screenshot-1.png)

renders the returned chart:

![](https://raw.github.com/danhaywood/isis-wicket-wickedcharts/master/images/screenshot-2.png)

#### Summary Charts

A collection with a `BigDecimal` property:
![](https://raw.github.com/danhaywood/isis-wicket-wickedcharts/master/images/summarychart-screenshot-1.png)

renders the returned chart with associated summary data:

![](https://raw.github.com/danhaywood/isis-wicket-wickedcharts/master/images/summarychart-screenshot-2.png)

If there was more than one property of type `BigDecimal`, then additional charts would be rendered for each such property.


## Scalar Chart Configuration and Usage

#### Configuration

In your project's parent `pom.xml`, add to the `<dependencyManagement>` section:

    <dependencyManagement>
        <dependencies>
            ...
            <dependency>
                <groupId>com.danhaywood.isis.wicket</groupId>
                <artifactId>danhaywood-isis-wicket-wickedcharts</artifactId>
                <version>x.y.z</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
            ...
        </dependencies>
    </dependencyManagement>

where `x.y.z` is the latest available version (search the [Maven Central Repo](http://search.maven.org/#search|ga|1|isis-wicket-wickedcharts)).


Add a dependency on the `applib` module In your project's DOM `pom.xml`:

    <dependencies>
        ...
        <dependency>
            <groupId>com.danhaywood.isis.wicket</groupId>
            <artifactId>danhaywood-isis-wicket-wickedcharts-applib</artifactId>
        </dependency>
        ...
    </dependencies> 

Finally, there is a required dependency in your project's webapp `pom.xml` to the `scalarchart` module:

    <dependencies>
        ...
        <dependency>
            <groupId>com.danhaywood.isis.wicket</groupId>
            <artifactId>danhaywood-isis-wicket-wickedcharts-scalarchart</artifactId>
        </dependency>
        ...
    </dependencies> 


#### Usage

This projects' `applib` module defines the `WickedChart` value type.  This is a simple wrapper around the Wicked Charts' project `Options` class.

Returning an instance of `WickedChart` from any action will render the chart.

For example, the screenshot above was generated using the following code (taken directly from the Wicked Charts' [getting started](https://code.google.com/p/wicked-charts/wiki/GettingStarted) page):

<pre>
public class ToDoItems {
    ...

    public WickedChart createChart() {
        Options options = new Options();

        options
            .setChartOptions(new ChartOptions()
                .setType(SeriesType.LINE));

        options
            .setTitle(new Title("My very own chart."));

        options
            .setxAxis(new Axis()
                .setCategories(Arrays
                    .asList(new String[] { "Jan", "Feb", "Mar", "Apr", "May",
                        "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" })));

        options
            .setyAxis(new Axis()
                .setTitle(new Title("Temperature (C)")));

        options
            .setLegend(new Legend()
                .setLayout(LegendLayout.VERTICAL)
                .setAlign(HorizontalAlignment.RIGHT)
                .setVerticalAlign(VerticalAlignment.TOP)
                .setX(-10)
                .setY(100)
                .setBorderWidth(0));

        options
            .addSeries(new SimpleSeries()
                .setName("Tokyo")
                .setData(
                    Arrays
                        .asList(new Number[] { 7.0, 6.9, 9.5, 14.5, 18.2, 21.5,
                            25.2, 26.5, 23.3, 18.3, 13.9, 9.6 })));

        options
            .addSeries(new SimpleSeries()
                .setName("New York")
                .setData(
                    Arrays
                        .asList(new Number[] { -0.2, 0.8, 5.7, 11.3, 17.0, 22.0,
                            24.8, 24.1, 20.1, 14.1, 8.6, 2.5 })));
        
        return new WickedChart(options);
    }
</pre>

While the demo shows a simple line graph, any chart supported by Wicket Charts (see their [showcase](http://wicked-charts.appspot.com/) app) should work.

# Limitations

Although the `WickedChart` has value semantics, it will not render as a chart if used as an entity property.

Such a property should be persistable, however.  Therefore a workaround is to hide the property and instead provide an action to show the chart.  For example:

<pre>
public class MyEntity {

    private WickedChart chart;
    @Hidden
    public WickedChart getChart() { ... }
    public void setChart(WickedChart chart) { ... }

    public WickedChart showChart() {
        return getChart();
    }
}
</pre>
    


## Summary Charts Configuration and Usage

#### Configuration

In your project's parent `pom.xml`, add to the `<dependencyManagement>` section:

    <dependencyManagement>
        <dependencies>
            ...
            <dependency>
                <groupId>com.danhaywood.isis.wicket.ui.components</groupId>
                <artifactId>danhaywood-isis-wicket-wickedcharts</artifactId>
                <version>1.0.0-SNAPSHOT</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
            ...
        </dependencies>
    </dependencyManagement>

Then, add the dependency in your project's webapp `pom.xml` to the `summarycharts` module:

    <dependencies>
        ...
        <dependency>
            <groupId>com.danhaywood.isis.wicket.ui.components</groupId>
            <artifactId>danhaywood-isis-wicket-wickedcharts-summarycharts</artifactId>
        </dependency>
        ...
    </dependencies> 

#### Usage

There is no special usage; a standalone collection of any entity with one or more properties of type `BigDecimal` will be rendered using the `summarycharts` extension.


## Legal Stuff

### License

    Copyright 2013 Dan Haywood

    Licensed under the Apache License, Version 2.0 (the
    "License"); you may not use this file except in compliance
    with the License.  You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing,
    software distributed under the License is distributed on an
    "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
    KIND, either express or implied.  See the License for the
    specific language governing permissions and limitations
    under the License.

### Dependencies

    <dependencies>

        <dependency>
            <!-- ASL v2.0 -->
            <groupId>org.apache.isis.core</groupId>
            <artifactId>isis-core-applib</artifactId>
            <version>${isis.version}</version>
        </dependency>

        <dependency>
            <!-- ASL v2.0 -->
            <groupId>commons-codec</groupId>
            <artifactId>commons-codec</artifactId>
            <version>1.7</version>
        </dependency>

        <dependency>
            <!-- ASL v2.0 -->
            <groupId>org.apache.isis.viewer</groupId>
            <artifactId>isis-viewer-wicket-ui</artifactId>
            <version>${isis-viewer-wicket.version}</version>
        </dependency>

        <dependency>
            <!-- ASL v2.0 -->
            <!-- in turn, depends on: 
                 * http://highcharts.com/license 
                   (commercial license required unless personal/open source project)  
             -->
            <groupId>com.googlecode.wicked-charts</groupId>
            <artifactId>wicked-charts-wicket6</artifactId>
            <version>${wicked-charts.version}</version>
        </dependency>

    </dependencies>
