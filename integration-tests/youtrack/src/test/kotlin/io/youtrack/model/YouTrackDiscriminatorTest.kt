package io.youtrack.model

import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertIs
import kotlinx.serialization.KSerializer
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class YouTrackDiscriminatorTest {
    private val json = Json {
        encodeDefaults = true
        ignoreUnknownKeys = true
    }

    @Test
    fun `read base discriminators match the spec`() {
        assertIs<ActivityItem.Default>(decodeRead(ActivityItem.serializer(), "ActivityItem"))
        assertIs<BaseArticleRead.Default>(decodeRead(BaseArticleRead.serializer(), "BaseArticle"))
        assertIs<BaseWorkItemRead.Default>(decodeRead(BaseWorkItemRead.serializer(), "BaseWorkItem"))
        assertIs<BundleElementRead.Default>(decodeRead(BundleElementRead.serializer(), "BundleElement"))
        assertIs<BundleRead.Default>(decodeRead(BundleRead.serializer(), "Bundle"))
        assertIs<ChangesProcessor.Default>(decodeRead(ChangesProcessor.serializer(), "ChangesProcessor"))
        assertIs<ColorCodingRead.Default>(decodeRead(ColorCodingRead.serializer(), "ColorCoding"))
        assertIs<CommandVisibilityRead.Default>(decodeRead(CommandVisibilityRead.serializer(), "CommandVisibility"))
        assertIs<CustomFieldConditionRead.Default>(decodeRead(CustomFieldConditionRead.serializer(), "CustomFieldCondition"))
        assertIs<CustomFieldDefaultsRead.Default>(decodeRead(CustomFieldDefaultsRead.serializer(), "CustomFieldDefaults"))
        assertIs<DatabaseAttributeValueRead.Default>(decodeRead(DatabaseAttributeValueRead.serializer(), "DatabaseAttributeValue"))
        assertIs<FilterFieldRead.Default>(decodeRead(FilterFieldRead.serializer(), "FilterField"))
        assertIs<IssueCustomFieldRead.Default>(decodeRead(IssueCustomFieldRead.serializer(), "IssueCustomField"))
        assertIs<IssueFolderRead.Default>(decodeRead(IssueFolderRead.serializer(), "IssueFolder"))
        assertIs<ProjectCustomFieldRead.Default>(decodeRead(ProjectCustomFieldRead.serializer(), "ProjectCustomField"))
        assertIs<SwimlaneSettingsRead.Default>(decodeRead(SwimlaneSettingsRead.serializer(), "SwimlaneSettings"))
        assertIs<UserRead.Default>(decodeRead(UserRead.serializer(), "User"))
        assertIs<UserGroupRead.Default>(decodeRead(UserGroupRead.serializer(), "UserGroup"))
        assertIs<VcsServer.Default>(decodeRead(VcsServer.serializer(), "VcsServer"))
        assertIs<VisibilityRead.Default>(decodeRead(VisibilityRead.serializer(), "Visibility"))
    }

    @Test
    fun `write base discriminators match the spec`() {
        assertWritesDiscriminator(BaseArticleWrite.serializer(), BaseArticleWrite.Default(), "BaseArticle")
        assertWritesDiscriminator(BaseWorkItemWrite.serializer(), BaseWorkItemWrite.Default, "BaseWorkItem")
        assertWritesDiscriminator(BundleElementWrite.serializer(), BundleElementWrite.Default(), "BundleElement")
        assertWritesDiscriminator(BundleWrite.serializer(), BundleWrite.Default, "Bundle")
        assertWritesDiscriminator(ColorCodingWrite.serializer(), ColorCodingWrite.Default, "ColorCoding")
        assertWritesDiscriminator(CommandVisibilityWrite.serializer(), CommandVisibilityWrite.Default, "CommandVisibility")
        assertWritesDiscriminator(CustomFieldConditionWrite.serializer(), CustomFieldConditionWrite.Default(), "CustomFieldCondition")
        assertWritesDiscriminator(CustomFieldDefaultsWrite.serializer(), CustomFieldDefaultsWrite.Default(), "CustomFieldDefaults")
        assertWritesDiscriminator(DatabaseAttributeValueWrite.serializer(), DatabaseAttributeValueWrite.Default, "DatabaseAttributeValue")
        assertWritesDiscriminator(FilterFieldWrite.serializer(), FilterFieldWrite.Default, "FilterField")
        assertWritesDiscriminator(IssueCustomFieldWrite.serializer(), IssueCustomFieldWrite.Default, "IssueCustomField")
        assertWritesDiscriminator(IssueFolderWrite.serializer(), IssueFolderWrite.Default(), "IssueFolder")
        assertWritesDiscriminator(ProjectCustomFieldWrite.serializer(), ProjectCustomFieldWrite.Default(), "ProjectCustomField")
        assertWritesDiscriminator(SwimlaneSettingsWrite.serializer(), SwimlaneSettingsWrite.Default(), "SwimlaneSettings")
        assertWritesDiscriminator(UserWrite.serializer(), UserWrite.Default, "User")
        assertWritesDiscriminator(UserGroupWrite.serializer(), UserGroupWrite.Default, "UserGroup")
        assertWritesDiscriminator(VisibilityWrite.serializer(), VisibilityWrite.Default, "Visibility")
    }

    private fun <T> decodeRead(serializer: KSerializer<T>, type: String): T =
        json.decodeFromString(serializer, """{"${'$'}type":"$type"}""")

    private fun <T> assertWritesDiscriminator(serializer: KSerializer<T>, value: T, type: String) {
        assertContains(
            json.encodeToString(serializer, value),
            "\"${'$'}type\":\"$type\"",
        )
    }
}
