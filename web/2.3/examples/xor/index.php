<?
include '../../util.php';
example("XOR", "Compute the logical XOR. Returns 1 if and only if exactly 1 of two input is 1. A pretty basic example what single layer network cannot do because the classification cannot be done by a single hyperplane (line in our 2D case). This is a very popular problem which caused lot of troubles to neural network research in the past, but it has been solved by the basic backpropagation algorithm very well. We simply use 2 layers. 2 neurons in the first layer make two hyperplanes and the single neuron in the last layer computes AND.");
?>
