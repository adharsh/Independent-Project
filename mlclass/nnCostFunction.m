function [Jcost grad] = nnCostFunction(neuralnetworkParameters, inputLayer, hiddenLayer, labelSize, X, y, lambda)

Theta1 = reshape(neuralnetworkParameters(1:hiddenLayer * (inputLayer + 1)), hiddenLayer, (inputLayer + 1));
Theta2 = reshape(neuralnetworkParameters((1 + (hiddenLayer * (inputLayer + 1))):end), labelSize, (hiddenLayer + 1));

m = size(X, 1);

Jcost = 0;
Theta1GradientDescent = zeros(size(Theta1));
Theta2GradientDescent = zeros(size(Theta2));

a1 = [ones(m, 1) X];

knl = eye(labelSize);
y = knl(y,:);

z2 = a1 * Theta1';
a2 = sigmoid(z2);

a2 = [ones(m, 1) a2];
z3 = a2 * Theta2';
a3 = sigmoid(z3);

Theta1Filtered = Theta1(:, 2:end);
Theta2Filtered = Theta2(:, 2:end);
Jcost = (1/m) .* sum(sum( (-y).*log(a3)-(1-y).*log(1-a3)  )) + lambda/(2*m) .* (sum(sum( Theta1Filtered.^2 )) + sum(sum( Theta2Filtered.^2 )));

accuDelta1 = 0;
accuDelta2 = 0;

deltaBackErr3 = a3 - y;
z2 = [ones(m,1) z2];

deltaBackErr2 = deltaBackErr3 * Theta2 .* sigmoidGradient(z2);
deltaBackErr2 = deltaBackErr2(:,2:end);

accuDelta1 = accuDelta1 + deltaBackErr2'*a1; 
accuDelta2 = accuDelta2 + deltaBackErr3'*a2; 

Theta1GradientDescent = (1/m) .* accuDelta1;
Theta2GradientDescent = (1/m) .* accuDelta2;

grad = [Theta1GradientDescent(:) ; Theta2GradientDescent(:)];

end