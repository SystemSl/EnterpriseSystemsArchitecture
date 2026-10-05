<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="html" encoding="UTF-8" indent="yes"/>

    <xsl:template match="/">
        <html>
            <head>
                <title>REST XML: Игроки</title>
                <link rel="stylesheet" href="/css/style.css"/>
            </head>
            <body>
                <div class="nav-bar">
                    <a href="/api/players">REST XML: Игроки</a>
                    <a href="/api/guilds">REST XML: Гильдии</a>
                </div>

                <h2>Список игроков (Клиентская XSLT-трансформация)</h2>

                <table>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Никнейм</th>
                            <th>Уровень</th>
                            <th>Класс</th>
                            <th>Раса</th>
                            <th>Гильдия (Навигация)</th>
                        </tr>
                    </thead>
                    <tbody>
                        <xsl:for-each select="//item | //Player">
                            <tr>
                                <td><xsl:value-of select="id"/></td>
                                <td><xsl:value-of select="nickname"/></td>
                                <td><xsl:value-of select="level"/></td>
                                <td><xsl:value-of select="characterClass"/></td>
                                <td><xsl:value-of select="race"/></td>
                                <td>
                                    <xsl:choose>
                                        <xsl:when test="guild/id">
                                            <a href="/api/guilds/{guild/id}">
                                                <xsl:value-of select="guild/name"/> (ID: <xsl:value-of select="guild/id"/>)
                                            </a>
                                        </xsl:when>
                                        <xsl:otherwise>Без гильдии</xsl:otherwise>
                                    </xsl:choose>
                                </td>
                            </tr>
                        </xsl:for-each>
                    </tbody>
                </table>
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>