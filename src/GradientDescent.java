import java.util.Scanner;

import org.ejml.data.DenseMatrix64F;
import org.ejml.ops.CommonOps;

public class GradientDescent {

	
	public static Object[] nnCostFunction( DenseMatrix64F theta1, DenseMatrix64F theta2, int labelSize, DenseMatrix64F X, DenseMatrix64F y, double lambda ){
		
		int m = X.getNumRows();

		double Jcost = 0;
		DenseMatrix64F theta1Grad = new DenseMatrix64F( theta1.getNumRows(), theta1.getNumCols() );
		DenseMatrix64F theta2Grad = new DenseMatrix64F( theta2.getNumRows(), theta2.getNumCols() );

		DenseMatrix64F thetaTranspose1 = new DenseMatrix64F( theta1.getNumCols(), theta1.getNumRows() );
		DenseMatrix64F thetaTranspose2 = new DenseMatrix64F( theta2.getNumCols(), theta2.getNumRows() );
		CommonOps.transpose( theta1, thetaTranspose1 );
		CommonOps.transpose( theta2, thetaTranspose2 );
		
		y = NNstaticmethods.categorizeClasses(y, labelSize);

		DenseMatrix64F a1 = NNstaticmethods.addBias( X );
		
		DenseMatrix64F z2 = new DenseMatrix64F( a1.getNumRows(), thetaTranspose1.getNumCols());
		CommonOps.mult( a1, thetaTranspose1, z2);
		
		DenseMatrix64F a2 = new DenseMatrix64F( z2.getNumRows(), z2.getNumCols());
		a2 = NNstaticmethods.sigmoid(z2);
		//valid
		
		a2 = NNstaticmethods.addBias(a2);
		DenseMatrix64F z3 = new DenseMatrix64F( a2.getNumRows(), thetaTranspose2.getNumCols());
		CommonOps.mult( a2, thetaTranspose2, z3);
		
		DenseMatrix64F a3 = new DenseMatrix64F( z3.getNumRows(), z3.getNumCols() );
		a3 = NNstaticmethods.sigmoid(z3);
		
		DenseMatrix64F theta1Filtered = NNstaticmethods.filter( theta1 );
		DenseMatrix64F theta2Filtered = NNstaticmethods.filter( theta2 );

		//		Jcost = (1/m) * sum(sum( (-y).*log(a3) - (1-y).*log(1-a3)  )) + lambda/(2*m) * ( sum(sum(Theta1Filtered.^2)) + sum(sum( Theta2Filtered.^2 )) );

		DenseMatrix64F yRep = new DenseMatrix64F( y.getNumRows(), y.getNumCols() );
		DenseMatrix64F a3Rep = new DenseMatrix64F( a3.getNumRows(), a3.getNumCols() );
		DenseMatrix64F prod1 = new DenseMatrix64F( a3.getNumRows(), a3.getNumCols() );
		DenseMatrix64F prod2 = new DenseMatrix64F( a3.getNumRows(), a3.getNumCols() );


		/******** (1/m) * sum(sum( (-y).*log(a3) - (1-y).*log(1-a3)  )) ******/

		// -y
		CommonOps.scale(-1.0, y, yRep);
		// log(a3)
		
		a3Rep = NNstaticmethods.log( a3 );
		
		// (-y).*log(a3) = prod1
		CommonOps.elementMult(yRep, a3Rep, prod1);
		
		// (1-y) = (1 + -1*y) = yRep
		CommonOps.add( yRep, 1.0);
		
		
		// (1-a3)
		CommonOps.subtract( 1.0, a3, a3Rep );
		
		
		// log(1-a3)
		a3Rep = NNstaticmethods.log( a3Rep );

		// (1-y).*log(1-a3) = prod2
		CommonOps.elementMult(yRep, a3Rep, prod2);
		
		// (-y).*log(a3) - (1-y).*log(1-a3) = prod1 - prod2 -> prod1
		CommonOps.subtract( prod1, prod2, prod1);

		// sum(sum( (-y).*log(a3) - (1-y).*log(1-a3)  ))
		Jcost = CommonOps.elementSum( prod1 );
		
		// (1/m) * ...
		Jcost = Jcost * (1.0/m);
		// up to here is valid
		
		/******* + lambda/(2*m) * ( sum(sum(Theta1Filtered.^2)) + sum(sum( Theta2Filtered.^2 )) ) ******/

		Jcost = Jcost + lambda/(2.0*m) * ( NNstaticmethods.elementSumSq(theta1Filtered) + NNstaticmethods.elementSumSq( theta2Filtered ) );
		
		/*
deltaBackErr3 = a3 - y;
z2 = [ones(m,1) z2];

deltaBackErr2 = deltaBackErr3 * Theta2 .* sigmoidGradient(z2);
deltaBackErr2 = deltaBackErr2(:,2:end);

accuDelta1 = accuDelta1 + deltaBackErr2'*a1; 
accuDelta2 = accuDelta2 + deltaBackErr3'*a2; 

Theta1GradientDescent = (1/m) * accuDelta1;
Theta2GradientDescent = (1/m) * accuDelta2;

grad = [Theta1GradientDescent(:) ; Theta2GradientDescent(:)];
		 */


		DenseMatrix64F accuDelta1;
		DenseMatrix64F accuDelta2;

		DenseMatrix64F deltaBackErr3 = new DenseMatrix64F( y.getNumRows(), y.getNumCols() );
		CommonOps.subtract(a3, y, deltaBackErr3);
		z2 = NNstaticmethods.addBias( z2 );

		DenseMatrix64F deltaBackErr2 = new DenseMatrix64F( deltaBackErr3.getNumRows(), theta2.getNumCols() );
		CommonOps.mult(deltaBackErr3, theta2, deltaBackErr2);
		CommonOps.elementMult( deltaBackErr2, NNstaticmethods.sigmoidGradient(z2), deltaBackErr2 );
		deltaBackErr2 = NNstaticmethods.filter( deltaBackErr2 );


		CommonOps.transpose(deltaBackErr2);
		DenseMatrix64F product1 = new DenseMatrix64F( deltaBackErr2.getNumRows(), a1.getNumCols());
		CommonOps.mult( deltaBackErr2, a1, product1 );
		accuDelta1 = new DenseMatrix64F( product1.getNumRows(), product1.getNumCols() );
		CommonOps.add(product1, accuDelta1, accuDelta1);

		CommonOps.transpose(deltaBackErr3);
		DenseMatrix64F product2 = new DenseMatrix64F( deltaBackErr3.getNumRows(), a2.getNumCols());
		CommonOps.mult( deltaBackErr3, a2, product2 );
		accuDelta2 = new DenseMatrix64F( product2.getNumRows(), product2.getNumCols() );
		CommonOps.add( product2, accuDelta2, accuDelta2 );

		CommonOps.scale( (1.0/m), accuDelta1, theta1Grad );
		CommonOps.scale( (1.0/m), accuDelta2, theta2Grad );

		CommonOps.subtract(theta1, theta1Grad, theta1);
		CommonOps.subtract(theta2, theta2Grad, theta2);

		Double Djcost = new Double(Jcost);
		
		Object[] nnCost = { Djcost, theta1, theta2 };

		return nnCost;
	}	

	
}