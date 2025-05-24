package com.naik.calculatorapp

import kotlin.math.*

object BlackScholesCalculator {

    fun calculateCallOptionPrice(
        S: Double, // Spot price
        K: Double, // Strike price
        T: Double, // Time to maturity in years
        r: Double, // Risk-free rate (annualized)
        sigma: Double // Volatility (annualized)
    ): Double {
        val d1 = (ln(S / K) + (r + 0.5 * sigma * sigma) * T) / (sigma * sqrt(T))
        val d2 = d1 - sigma * sqrt(T)
        return S * cumulativeNormalDistribution(d1) - K * exp(-r * T) * cumulativeNormalDistribution(d2)
    }

    fun calculatePutOptionPrice(
        S: Double, // Spot price
        K: Double, // Strike price
        T: Double, // Time to maturity in years
        r: Double, // Risk-free rate (annualized)
        sigma: Double // Volatility (annualized)
    ): Double {
        val d1 = (ln(S / K) + (r + 0.5 * sigma * sigma) * T) / (sigma * sqrt(T))
        val d2 = d1 - sigma * sqrt(T)
        return K * exp(-r * T) * cumulativeNormalDistribution(-d2) - S * cumulativeNormalDistribution(-d1)
    }

    fun calculateSpotPrice(
        optionPrice: Double,
        K: Double,
        T: Double,
        r: Double,
        sigma: Double,
        isCallOption: Boolean
    ): Double {
        if (T <= 0 || sigma <= 0 || optionPrice <= 0) {
            return 0.0
        }

        // Initial guess for spot price
        var S = K
        var priceDiff = 1.0
        val epsilon = 0.0001 // Convergence threshold
        val maxIterations = 100
        var iterations = 0

        while (abs(priceDiff) > epsilon && iterations < maxIterations) {
            val currentPrice = if (isCallOption) {
                calculateCallOptionPrice(S, K, T, r, sigma)
            } else {
                calculatePutOptionPrice(S, K, T, r, sigma)
            }
            priceDiff = currentPrice - optionPrice

            // Calculate derivative (delta)
            val d1 = (ln(S / K) + (r + 0.5 * sigma * sigma) * T) / (sigma * sqrt(T))
            val delta = if (isCallOption) {
                cumulativeNormalDistribution(d1)
            } else {
                -cumulativeNormalDistribution(-d1)
            }

            // Newton-Raphson update
            S = S - priceDiff / delta
            iterations++
        }

        return S
    }

    // Approximation of the cumulative normal distribution function
    private fun cumulativeNormalDistribution(x: Double): Double {
        val t = 1.0 / (1.0 + 0.2316419 * abs(x))
        val d = 0.3989423 * exp(-x * x / 2.0)
        val prob = d * (0.319381530 + t * (-0.356563782 + t * (1.781477937 + t * (-1.821255978 + t * 1.330274429))))
        return if (x > 0) 1.0 - prob else prob
    }
}