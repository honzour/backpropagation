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
 Large networks can do lot of very interesting stuff. They can detect some object in a picture, make a weather forecast, predict the next word in a text, transform a sound of a talk to a text etc. Network calculation is very simple. So what is the problem? Each neuron has a weight vector and treshold. There is no obvious method how to set weights and tresholds for each problem. Another (smaller) problem is the network anatomy. There is no simple magic rule to set the neuron count in each layer.
</P>
<P>
In neural networking (unlike common programming approach) we normally do not exacly know what we want. We just have some set of examples what we want. For example in weather forecast one day forward case, we have set of temperatures, wind power and directions etc. on several places for days <I>d</I> (inputs) and weathers in some place for days <I>d + 1</I> (outputs). We are not able to write an exact mathematical formula like <I>output = f(input)</I> but we have set of many pairs <I>(input<SUB>i<SUB>, ouput<SUB>i<SUB>)</I> which is called a training set. All we want is to set layers count and neuron in each layer count to some appropriate number (this can be done by hand for each task) and set all weights and tresholds so that for each <I>input<SUB>i<SUB></I> in the training set our network calculates its <I>output<SUB>i<SUB></I>. (This is the hardest problem and cannot be done by hand.) If neuron count is not to big then the network has no capacity to learn the training set as a table, it must exstract some rules from the training set data - it must generalize. So we hope that the well trained network will give good ouput even for data that are not in the training set.
</P>
<H3>Gradient method</H3>
<P>
We have a training set, the network with weights and tresholds set to random numbers and all we want is to set weights and treshols so that the network will not fail on out training set. But how to do it? First of all we resign on beeing perfect. Network output and the exact output in trainig set may differ, but the difference must be small. We define a partial error function for training set example i and output neuron with index j E<SUB>ij</SUB> = (real_output<SUB>ij</SUB> - expected_output<SUB>ij</SUB>)<SUP>2</SUP>. The whole error E is sum of all (all training set examples, all output neurons) such E<SUB>ij</SUB>. E is always greater or equal to zero because it is a sum of squares
</P>
<H3>Backpropagation</H3>
<P>
</P>

</BODY>
</HTML>
