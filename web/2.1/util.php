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

function exampleCsvXmlPng()
{
// TODO
?>
<UL>
<LI><A HREF="backpropagation://backpropagation.moxo.cz/2.1/examples/3d/network.csv">Import the problem to be learned</A></LI>
<LI><A HREF="backpropagation://backpropagation.moxo.cz/2.1/examples/3d/network.xml">Import the solution</A></LI>
<LI><A HREF="network.csv">View the problem CSV file</A></LI>
<LI><A HREF="network.xml">View the solution XML file</A></LI>
<LI><BR><IMG SRC="result.png">
<BR>Result visualisation</LI>
<LI><BR><IMG SRC="network.png">
<BR>Network visualisation</LI>
</UL>
<?
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

?>
