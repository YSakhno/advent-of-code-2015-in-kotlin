apply<IdeaPlugin>()

configure<org.gradle.plugins.ide.idea.model.IdeaModel> {
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}
