package edu.utk.phys.astro.hubble;

import org.apache.commons.math.ode.ContinuousOutputModel;
import org.apache.commons.math.ode.DerivativeException;
import org.apache.commons.math.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math.ode.FirstOrderIntegrator;
import org.apache.commons.math.ode.IntegratorException;
import org.apache.commons.math.ode.nonstiff.DormandPrince853Integrator;

import android.util.Log;

public class HarmonicODE implements FirstOrderDifferentialEquations {

	private double k = 0.1;
	ContinuousOutputModel com = new ContinuousOutputModel();
	
	
	public void compute(double[] y0, double t0, double tmax) throws DerivativeException, IntegratorException {
		FirstOrderIntegrator dp853 = new DormandPrince853Integrator(1.0e-8, 100.0, 1.0e-10, 1.0e-10);
		
		com = new ContinuousOutputModel();
		dp853.addStepHandler(com);
		dp853.integrate(this, 0.0, y0, 16.0, y0);
		Log.i("HarmonicODE","y0[0] = " + y0[0] + ", y0[1] = " + y0[1]);
	}
	
	public double[] getResult(double t) throws DerivativeException {
		this.com.setInterpolatedTime(t);
		return this.com.getInterpolatedState();
	}
	
	// Define: x = k*d^2 x/ dt^2
	// y0 = x
	// y1 = dx/dt = x0'
	// -> x = k*d^2x/dt = k*d(y1)/dt = k*x1' = x0
	@Override
	public void computeDerivatives(double t, double[] y, double[] yDot)
			throws DerivativeException {
		
		yDot[0] = y[1];
		yDot[1] = -(1/k)*y[0];
		
	}

	@Override
	public int getDimension() {
		return 2;
	}

}
