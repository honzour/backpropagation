<?
include '../../util.php';
example("Chessboard 2x2 - 2-2-2-1", "We have a chess board 2x2 and we want to return 1 on any pixel of the white field and 0 on any pixel of the black field. Looks but simple it seems to be a challenge for a basic backpropagation. It cannot be done by 1 layer network but also 2 layers is not enough. We must place hyperplanes between the fields (1 layer) and it reduces the problem to XOR (2 more layers). So we need 3 layers to compute this. And it is even worse, there is a pretty deep local minimum of the error function when we place hyperplanes in the first layer like in the basic XOR... Simple problem but hard for the backpropagation algorithm. The current version fails here if the anatomy is 2-2-2-1, but there must be some way to teach the network algoritmically. It is on my TODO list.");
?>

