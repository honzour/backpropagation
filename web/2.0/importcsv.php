<HTML>
<HEAD>
<META charset="utf-8">
<TITLE>Backpropagation - Import from CSV</TITLE>
</HEAD>
<BODY>
<?
include 'google.php'
?>
<H2>Import from CSV</H2>
<P>
This program supports import of the network anatomy and training set from a CSV file. You can load it back later from the New task screen. You can load the file from the SD card or from anywhere else on the local filesystem where this program has access rights. You can also send by an e-mail by some application configured in you Android OS.
</P>
<P>
CSV means 'Comma Separated Values' so it is just a simple text format that can be created in any text editor and also simply exported from you favorite spreadseet program. Lines started by semicolon character ';' are comments. There is a format version, layer sizes and the training set, each element in the format 'i,n,p,u,t,,o,u,t,p,u,t'. The format is sensitive to extra lines, white spaces, extra or missing commas etc. Unlike XML format, current weights status is not stored, so this is just a problem file format, not current training status file format.
</P>
<PRE>
;Format
CSV1
;Anatomy
2,2,1
;Training set
0.0,0.0,,0.0
0.0,1.0,,1.0
1.0,0.0,,1.0
1.0,1.0,,0.0
</PRE>
<?
include 'links.php'
?>
</BODY>
</HTML>

