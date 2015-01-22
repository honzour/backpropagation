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
<IMG SRC="neuron.png" ALT="neuron">
<P>
The perceptron is a common model of a neuron. It has a single output (which can be one of the outputs the whole neural network or one of inputs of one or more other perceptrons), a single treshold and several inputs (which are subset of inputs of the whole network or outputs of other neurons). Each input has its weight. Percepron calculates weighted sum of all inputs minus threshold. The result is called potential. The basic model returns simply one if potential is greater than zero and zero otherwise. The more advanced models calculates some other function of potential, the most common function called sigma is 1 / (1 + exp(-p)), which is anyway just continuous version of the previous case, because it returns values from zero to one. It also has positive derivation everywhere which makes gradient learning method possible.
</P>
<IMG SRC="sigma.png" ALT="sigma">
<P>
Simple perceptron calculates
</P> 
<P><I>x<SUB>1</SUB>w<SUB>1</SUB> + x<SUB>2</SUB>w<SUB>2</SUB> + ... + x<SUB>n</SUB>w<SUB>n</SUB> &gt; t</I></P>
<P>
which is just split of the space by a singe hyperline into two halfspaces. Neuron with sigma function does the same but with some level of uncertainty.
</P>
<H3>Neural network</H3>
<P>
There are many ways how to connect neurons into a network. The most common are networks split into layers. Input of each neuron in the first layer is input of the whole network, output of neurons in the last layer is output of the whole network. Otherwise input of each neuron in the layer <I>i</I> is output of the whole layer <I>i - 1</I> So this means that there are just connection between neighbour layers, there are no other connection, even neurons in the same layer are not connected with each other. There is also no cycle. Network calculation is simple. We put the input into each neuron in the first layer, they calculate its output. We take the first layer output vector and put it as input of each neuron in the second layer. Then we calculate output of the second layer and put it as the input of the third layer etc. 
</P>
<IMG SRC="network.png" ALT="network">
<H3>Problem with learning</H3>
<P>
 Large networks can do lot of very interesting stuff. They can detect some object in a picture, make a weather forecast, predict the next word in a text, transform a sound of a talk to a text etc. Network calculation is very simple. So what is the problem? Each neuron has a weight vector and a treshold. There is no obvious method how to set weights and tresholds for each problem. Another (smaller) problem is the network anatomy. There is no simple magic rule to set the neuron count in each layer.
</P>
<P>
In neural networking (unlike common programming approach) we normally do not exacly know what we want. We just have some set of examples what we want. For example in weather forecast one day forward case, we have set of temperatures, wind powers and directions etc. on several places for days <I>d</I> (inputs) and weathers in some place for days <I>d + 1</I> (outputs). We are not able to write an exact mathematical formula like <I>output = f(input)</I> but we have set of many pairs <I>(input<SUB>i</SUB>, ouput<SUB>i</SUB>)</I> which is called a training set. All we want is to set layers count and neuron in each layer count to some appropriate number (this can be done by hand for each task) and set all weights and tresholds so that for each <I>input<SUB>i</SUB></I> in the training set our network calculates its <I>output<SUB>i</SUB></I>. (This is the hardest problem and cannot be done by hand.) If neuron count is not to big then the network has no capacity to learn the training set as a table, it must extract some rules from the training set data - it must generalize. So we hope that the well trained network will give good ouput even for data that are not in the training set.
</P>
<P>
We have a training set, the network with weights and tresholds set to random numbers and all we want is to set weights and treshols so that the network will not fail on out training set. But how to do it? First of all we resign on beeing perfect. Network output and the exact output in trainig set may differ, but the difference must be small. We define a partial error function for training set example <I>i</I> and output neuron with index <I>j</I>:
</P>
<P>
<I>E<SUB>ij</SUB> = (real_output<SUB>ij</SUB> - expected_output<SUB>ij</SUB>)<SUP>2</SUP></I>
</P>
<P>
The whole error E is sum of all (all training set examples, all output neurons) such E<SUB>ij</SUB>. E is always greater or equal to zero because it is a sum of squares. We want to set all the weights and tresholds to get E very small.
</P>
<H3>Gradient method</H3>
<P>
We are simply looking for a minimum of the E function on some multi dimensional space and we know that this is a hard problem. The most common heuristic in such cases is the gradient method. Imagine you are somewhere in the country, there is a fog and almost no visibility and you want to find the highest mountin there. So you would propably go direct up while it would be possible. You would probably reach some hill which may or may not be the highest mountain. This is exactly gradient method. We calculate partial derivation of E for every weight and treshols and then we move it in the direction of the gradient and it is all.
</P>
<H3>Backpropagation</H3>
<P>
The Backpropagation algorithm is just a gradient method for learning neural networks. Sum has a derivation, product has a derivation, Neuron sigma function has a derivation, composed function has a derivation etc., so it is possible to get all partial derivation of E. It looks it will be difficult but it will not be. We can calculate it for each training set element individualy and then summarize it. Because of the properties of the sigma function the calculation for each element is quite simple, we go from the last layer backwards and the derivations value in the previous layer can be calculated from the (already calculated) next one. If you like calculus and are not satisfied by my simple explanation, please watch some chapters from <A HREF="https://www.coursera.org/course/neuralnets">this coursera course</A>.
</P>
<?
include 'links.php'
?>
</BODY>
</HTML>

