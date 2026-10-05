<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="html" encoding="UTF-8" indent="yes"/>

    <xsl:template match="/">
        <html>
            <head>
                <title>REST XML: Гильдии</title>
                <link rel="stylesheet" href="/css/style.css"/>
            </head>
            <body>
                <div class="nav-bar">
                    <a href="/api/players">REST XML: Игроки</a>
                    <a href="/api/guilds">REST XML: Гильдии</a>
                </div>

                <h2>Список гильдий (Клиентская XSLT-трансформация)</h2>

                <table>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Название</th>
                            <th>Рейтинг</th>
                            <th>Лидер (Навигация)</th>
                            <th>Дата создания</th>
                            <th>Описание</th>
                        </tr>
                    </thead>
                    <tbody>
                        <xsl:for-each select="//item | //Guild">
                            <tr>
                                <td><xsl:value-of select="id"/></td>
                                <td><xsl:value-of select="name"/></td>
                                <td><xsl:value-of select="rating"/></td>
                                <td>
                                    <xsl:choose>
                                        <xsl:when test="leader/id">
                                            <a href="/api/players/{leader/id}">
                                                <xsl:value-of select="leader/nickname"/> (lvl <xsl:value-of select="leader/level"/>)
                                            </a>
                                        </xsl:when>
                                        <xsl:otherwise>Нет лидера</xsl:otherwise>
                                    </xsl:choose>
                                </td>
                                <td><xsl:value-of select="createdAt"/></td>
                                <td><xsl:value-of select="description"/></td>
                            </tr>
                        </xsl:for-each>
                    </tbody>
                </table>
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>