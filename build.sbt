lazy val commonSettings = Seq(
  version := "0.1.0",
  scalacOptions += "-deprecation"
)

lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  settings(
  )
