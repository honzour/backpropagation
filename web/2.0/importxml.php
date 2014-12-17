<HTML>
<HEAD>
<META charset="utf-8">
<TITLE>Backpropagation - Import from XML</TITLE>
</HEAD>
<BODY>
<?
include 'google.php'
?>
<H2>Import from XML</H2>
<P>
This program supports import of the network (including current weights) from a xml file. You can load the file from the SD card or from anywhere else on the local filesystem where this program has access rights. You can also download it from the www by a button press here or from the native android browser by the special backpropagation:// URL scheme handling.
</P>
<P>
XML is a text format, so the data can be created in any text editor and it is quite well human readable for small networks. But even for small networks it is quite lot of work. If you want to load just the network anatomy and training set and not each weight (training status), use CSV file format.
</P>
<PRE>
&lt;?xml version=&quot;1.0&quot; encoding=&quot;UTF-8&quot; ?&gt;
&lt;network&gt;
	&lt;layers&gt;
		&lt;layer&gt;
			&lt;neuron&gt;
				&lt;weight&gt;6.066138684003899&lt;/weight&gt;
				&lt;weight&gt;6.269257667733099&lt;/weight&gt;
				&lt;weight&gt;6.2692576304742955&lt;/weight&gt;
			&lt;/neuron&gt;
			&lt;neuron&gt;
				&lt;weight&gt;-6.056981147271498&lt;/weight&gt;
				&lt;weight&gt;6.056733046715105&lt;/weight&gt;
				&lt;weight&gt;6.0567330147589455&lt;/weight&gt;
			&lt;/neuron&gt;
		&lt;/layer&gt;
		&lt;layer&gt;
			&lt;neuron&gt;
				&lt;weight&gt;-14.294402326127392&lt;/weight&gt;
				&lt;weight&gt;28.88134478616663&lt;/weight&gt;
				&lt;weight&gt;-29.10725683324868&lt;/weight&gt;
			&lt;/neuron&gt;
		&lt;/layer&gt;
	&lt;/layers&gt;
	&lt;training&gt;
		&lt;inputs&gt;
			&lt;input&gt;
				&lt;number&gt;0.0&lt;/number&gt;
				&lt;number&gt;0.0&lt;/number&gt;
			&lt;/input&gt;
			&lt;input&gt;
				&lt;number&gt;0.0&lt;/number&gt;
				&lt;number&gt;1.0&lt;/number&gt;
			&lt;/input&gt;
			&lt;input&gt;
				&lt;number&gt;1.0&lt;/number&gt;
				&lt;number&gt;0.0&lt;/number&gt;
			&lt;/input&gt;
			&lt;input&gt;
				&lt;number&gt;1.0&lt;/number&gt;
				&lt;number&gt;1.0&lt;/number&gt;
			&lt;/input&gt;
		&lt;/inputs&gt;
		&lt;outputs&gt;
			&lt;output&gt;
				&lt;number&gt;0.0&lt;/number&gt;
			&lt;/output&gt;
			&lt;output&gt;
				&lt;number&gt;1.0&lt;/number&gt;
			&lt;/output&gt;
			&lt;output&gt;
				&lt;number&gt;1.0&lt;/number&gt;
			&lt;/output&gt;
			&lt;output&gt;
				&lt;number&gt;0.0&lt;/number&gt;
			&lt;/output&gt;
		&lt;/outputs&gt;
		&lt;outputs&gt;
		&lt;/outputs&gt;
	&lt;/training&gt;
&lt;/network&gt;
</PRE>
<?
include 'links.php'
?>
</BODY>
</HTML>

