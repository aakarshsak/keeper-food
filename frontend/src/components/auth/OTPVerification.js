import React, { useState, useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import './Auth.css';

const OTPVerification = () => {
  const [otp, setOtp] = useState('');
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [loading, setLoading] = useState(false);
  const [resendLoading, setResendLoading] = useState(false);
  const [countdown, setCountdown] = useState(0);
  
  const { verifyEmail, resendVerification } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  
  const email = location.state?.email || '';
  const initialMessage = location.state?.message || '';

  useEffect(() => {
    if (initialMessage) {
      setSuccess(initialMessage);
    }
  }, [initialMessage]);

  useEffect(() => {
    if (countdown > 0) {
      const timer = setTimeout(() => setCountdown(countdown - 1), 1000);
      return () => clearTimeout(timer);
    }
  }, [countdown]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    if (otp.length !== 6) {
      setError('Please enter a valid 6-digit OTP');
      setLoading(false);
      return;
    }

    const result = await verifyEmail({ email, otp });
    
    if (result.success) {
      setSuccess('Email verified successfully! Redirecting to login...');
      setTimeout(() => {
        navigate('/login');
      }, 2000);
    } else {
      setError(result.error);
    }
    
    setLoading(false);
  };

  const handleResendOTP = async () => {
    setResendLoading(true);
    setError('');
    setSuccess('');

    const result = await resendVerification(email);
    
    if (result.success) {
      setSuccess('OTP sent successfully! Please check your email.');
      setCountdown(60); // 60 seconds countdown
    } else {
      setError(result.error);
    }
    
    setResendLoading(false);
  };

  const handleOtpChange = (e) => {
    const value = e.target.value.replace(/\D/g, ''); // Only digits
    if (value.length <= 6) {
      setOtp(value);
    }
  };

  if (!email) {
    return (
      <div className="auth-container">
        <div className="auth-card">
          <div className="auth-header">
            <h1>Email Required</h1>
            <p>Please register first to verify your email.</p>
          </div>
          <button 
            onClick={() => navigate('/register')}
            className="auth-button primary"
          >
            Go to Registration
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="auth-container">
      <div className="auth-card">
        <div className="auth-header">
          <h1>Verify Your Email</h1>
          <p>We've sent a verification code to</p>
          <strong>{email}</strong>
        </div>

        {error && <div className="auth-error">{error}</div>}
        {success && <div className="auth-success">{success}</div>}

        <form onSubmit={handleSubmit} className="auth-form">
          <div className="form-group">
            <label htmlFor="otp">Enter 6-digit OTP</label>
            <input
              type="text"
              id="otp"
              name="otp"
              value={otp}
              onChange={handleOtpChange}
              required
              placeholder="000000"
              className="otp-input"
              maxLength="6"
            />
          </div>

          <button 
            type="submit" 
            className="auth-button primary"
            disabled={loading || otp.length !== 6}
          >
            {loading ? 'Verifying...' : 'Verify Email'}
          </button>
        </form>

        <div className="auth-divider">
          <span>Didn't receive the code?</span>
        </div>

        <button 
          onClick={handleResendOTP}
          className="auth-button secondary"
          disabled={resendLoading || countdown > 0}
        >
          {resendLoading ? 'Sending...' : 
           countdown > 0 ? `Resend in ${countdown}s` : 'Resend OTP'}
        </button>

        <div className="auth-links">
          <p>
            Wrong email? <span 
              onClick={() => navigate('/register')}
              className="link-button"
            >
              Go back to registration
            </span>
          </p>
        </div>
      </div>
    </div>
  );
};

export default OTPVerification; 