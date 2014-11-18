<HTML>
<HEAD>
<META charset="utf-8">
<TITLE>Backpropagation algorithm</TITLE>
</HEAD>
<BODY>
<?
include 'google.php'
?>
<H2>Backpropagation algorithm</H2>
<P>
This is just a very basic introduction to artifitial neural networks in general and backpropagation algorithm.
</P>
<H3>Perceptron</H3>
<P>
The perceptron is a common model of a neuron. It has a single output (which can be one of the outputs the whole neural network or one of inputs of one or more other perceptrons), a single treshold and several inputs (which are subset of inputs of the whole network or outputs of other neurons). Each input has its weight. Percepron calculates weighted sum of all inputs minus threshold. The result is called potential. The basic model returns simply one if potential is greater than zero and zero otherwise. The more advanced models calculates some other function of potential, the most common function called sigma is 1 / (1 + exp(-p)), which is anyway just continuous version of the previous case, because it returns values from zero to one. It also has positive derivation everywhere which makes gradient learning method possible.
</P>
<P>
Simple perceptron calculates
</P> 
<P>x<SUB>1</SUB>w<SUB>1</SUB> + x<SUB>2</SUB>w<SUB>2</SUB> + ... + x<SUB>n</SUB>w<SUB>n</SUB> &gt; t</P>
<P>
which is just split of the space by a singe hyperline into two halfspaces. Neuron with sigma function does the same but with some level of uncertainty.
</P>
<H3>Neural network</H3>
<P>
There are many ways how to connect neurons into a network. The most common are networks split into layers. Input of each neuron in the first layer is input of the whole network, output of neurons in the last layer is output of the whole network. Otherwise input of each neuron in the layer <I>i</I> is output of the whole layer <I>i - 1</I> So this means that there are just connection between neighbour layers, there are no other connection even neurons in the same layer are not connected with each other. There is also no cycle. Network calculation is simple. We put the input into each neuron in the first layer, they calculate its output. We take the first layer output vector and put it as input of each neuron in the second layer. Then we calculate output of the second layer and put it as the input of the third layer etc. 
</P>
<H3>Problem with learning</H3>
<P>
 Large networks can do lot of very interesting stuff. They can detect some object in a picture, make a weather forecast, predict the next word in a text, transform a sound of a talk to a text etc. Network calculation is very simple. So what is the problem? Each neuron has a weight vector and treshold. There is no obvious method how to set weights and tresholds for each problem. 
</P>
<H3>Gradient method</H3>
<P>
</P>
<H3>Backpropagation</H3>
<P>
</P>

</BODY>
</HTML>
