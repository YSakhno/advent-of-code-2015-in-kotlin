apply<EclipsePlugin>()

configure<org.gradle.plugins.ide.eclipse.model.EclipseModel> {
    classpath {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}
