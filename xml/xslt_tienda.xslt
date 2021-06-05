<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:template match="/">
<html> 
<body>
  <h2>Shop</h2>
  <table border="1">
    <tr bgcolor="#9acd32">
      <th style="text-align:left">Location</th>
      <th style="text-align:left">Timetable</th>
    </tr>
    <xsl:for-each select="tiendas/tienda">
    <tr>
      <td><xsl:value-of select="localizacion"/></td>
      <td><xsl:value-of select="horario"/></td>
    </tr>
    </xsl:for-each>
  </table>
</body>
</html>
</xsl:template>
</xsl:stylesheet>