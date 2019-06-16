//SBT assembly plugin is to create a fat JAR of your project with all of its dependencies.
addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.6")
//SBT docker plugin is to create Docker images.
addSbtPlugin("se.marcuslonnberg" % "sbt-docker" % "1.4.1")
//SBT scalastyle plugin is to examines your Scala code and indicates potential problems with it.
addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "1.0.0")
//SBT Sonar plugin
addSbtPlugin("com.olaq" % "sbt-sonar-scanner-plugin" % "1.3.0")