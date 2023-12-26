package io.raemian.infra.logging.logger

import com.slack.api.Slack
import com.slack.api.model.block.Blocks.asBlocks
import com.slack.api.model.block.Blocks.divider
import com.slack.api.model.block.Blocks.header
import com.slack.api.model.block.Blocks.richText
import com.slack.api.model.block.Blocks.section
import com.slack.api.model.block.HeaderBlock
import com.slack.api.model.block.RichTextBlock
import com.slack.api.model.block.SectionBlock
import com.slack.api.model.block.composition.BlockCompositions.markdownText
import com.slack.api.model.block.composition.BlockCompositions.plainText
import com.slack.api.model.block.element.BlockElements.asElements
import com.slack.api.model.block.element.BlockElements.asRichTextElements
import com.slack.api.model.block.element.BlockElements.richTextPreformatted
import com.slack.api.model.block.element.BlockElements.richTextSection
import com.slack.api.model.block.element.RichTextSectionElement.Text
import com.slack.api.model.block.element.RichTextSectionElement.TextStyle
import com.slack.api.webhook.Payload.PayloadBuilder
import com.slack.api.webhook.WebhookPayloads.payload
import io.raemian.infra.logging.enums.ErrorLocationEnum
import io.raemian.infra.logging.enums.LogTemplate
import io.raemian.infra.logging.enums.LogTemplate.APP_NAME
import io.raemian.infra.logging.enums.LogTemplate.ERROR_HEADER
import io.raemian.infra.logging.enums.LogTemplate.ERROR_MESSAGE
import io.raemian.infra.logging.enums.LogTemplate.REFERER
import io.raemian.infra.logging.enums.LogTemplate.USERAGENT
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class SlackLogger(
    private val slack: Slack = Slack.getInstance(),
) {
    @Value("\${slack.webhook.url}")
    private lateinit var url: String

    @Async
    fun error(
        errorLocation: ErrorLocationEnum,
        appName: String,
        errorMessage: String = "EMPTY VALUE",
        userAgent: String? = "",
        referer: String? = "",
    ) {
        when (errorLocation) {
            ErrorLocationEnum.CLIENT -> clientError(errorLocation, appName, errorMessage, userAgent, referer)
            else -> defaultError(errorLocation, appName, errorMessage)
        }
    }

    private fun clientError(
        errorLocation: ErrorLocationEnum,
        appName: String,
        errorMessage: String,
        userAgent: String?,
        referer: String?,
    ) {
        slack.send(
            url,
            payload { p: PayloadBuilder ->
                p.text(LogTemplate.ERROR_HEADER.of(errorLocation.value))
                    .blocks(
                        asBlocks(
                            createHeaderBlock(errorLocation),
                            divider(),
                            createAppNameBlock(appName),
                            createUserAgentBlock(userAgent),
                            createRefererBlock(referer),
                            createErrorMessageBlock(errorMessage),
                            divider(),

                        ),
                    )
            },
        )
    }

    private fun defaultError(
        errorLocation: ErrorLocationEnum,
        appName: String,
        errorMessage: String,
    ) {
        slack.send(
            url,
            payload { p: PayloadBuilder ->
                p.text(ERROR_HEADER.of(errorLocation.value))
                    .blocks(
                        asBlocks(
                            createHeaderBlock(errorLocation),
                            divider(),
                            createAppNameBlock(appName),
                            createErrorMessageBlock(errorMessage),
                            divider(),
                        ),
                    )
            },
        )
    }

    private fun createHeaderBlock(errorLocation: ErrorLocationEnum): HeaderBlock {
        return header { header ->
            header.text(plainText(ERROR_HEADER.of(errorLocation.value)))
        }
    }

    private fun createAppNameBlock(appName: String): SectionBlock {
        return section { section: SectionBlock.SectionBlockBuilder ->
            section.text(
                markdownText(APP_NAME.of(appName)),
            )
        }
    }

    private fun createUserAgentBlock(userAgent: String?): SectionBlock {
        return section { section: SectionBlock.SectionBlockBuilder ->
            section.text(
                markdownText(USERAGENT.of(userAgent)),
            )
        }
    }

    private fun createRefererBlock(referer: String?): SectionBlock {
        return section { section: SectionBlock.SectionBlockBuilder ->
            section.text(
                markdownText(REFERER.of(referer)),
            )
        }
    }

    private fun createErrorMessageBlock(errorMessage: String): RichTextBlock {
        return richText { richText ->
            richText.elements(
                asElements(
                    richTextSection { section ->
                        section.elements(
                            asRichTextElements(
                                Text.builder().text(ERROR_MESSAGE.message).style(TextStyle.builder().bold(true).build()).build(),
                            ),
                        )
                    },
                    richTextPreformatted { section ->
                        section.elements(
                            asRichTextElements(
                                Text.builder().text(errorMessage).build(),
                            ),
                        )
                    },
                ),
            )
        }
    }
}
