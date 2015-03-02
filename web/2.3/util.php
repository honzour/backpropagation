<?
function exampleHeader($title)
{
?>
<HTML>
<HEAD>
<META charset="utf-8">
<TITLE>Backpropagation - Examples - <?echo $title;?></TITLE>
</HEAD>
<BODY>
<?
}

function exampleGoogle()
{
include '../../google.php';
}

function exampleText($title, $text)
{
?>
<H2><? echo $title; ?></H2>
<P>
<? echo $text; ?>
</P>
<?
}

function exampleCsvXmlPngBool($showXml)
{
$root = "backpropagation://$_SERVER[HTTP_HOST]$_SERVER[REQUEST_URI]network.";
?>
<UL>
<LI><A HREF="<? echo $root; ?>csv">Import the problem to be learned</A></LI><? 
if ($showXml) { ?>
<LI><A HREF="<? echo $root; ?>xml">Import the solution</A></LI><? } ?>
<LI><A HREF="network.csv">View the problem CSV file</A></LI><? 
if ($showXml) { ?>
<LI><A HREF="network.xml">View the solution XML file</A></LI><? } ?>
<LI><BR><IMG SRC="result.png">
<BR>Result visualisation</LI>
<LI><BR><IMG SRC="network.png">
<BR>Network visualisation</LI>
</UL>
<?
}


function exampleCsvXmlPng()
{
  exampleCsvXmlPngBool(true);
}

function exampleFooter()
{
include '../../links.php'
?>
</BODY>
</HTML>
<?
}


function example($title, $text)
{
  exampleHeader($title);
  exampleGoogle();
  exampleText($title, $text);
  exampleCsvXmlPng();
  exampleFooter();
}

function exampleBool($title, $text, $showXml)
{
  exampleHeader($title);
  exampleGoogle();
  exampleText($title, $text);
  exampleCsvXmlPngBool($showXml);
  exampleFooter();
}

?>
