import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import './Auth.css';

const ForgotPassword = () => {
  const [step, setStep] = useState(1); // 1: Email, 2: OTP + New Password
  const [formData, setFormData] = useState({
    email: '',
    otp: '',
    newPassword: '',
    confirmPassword: ''
  });
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [loading, setLoading] = useState(false);
  
  const { forgotPassword, resetPassword } = useAuth();
  const navigate = useNavigate();

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleEmailSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    const result = await forgotPassword(formData.email);
    
    if (result.success) {
      setSuccess('OTP sent to your email! Please check your inbox.');
      setStep(2);
    } else {
      setError(result.error);
    }
    
    setLoading(false);
  };

  const handlePasswordReset = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    if (formData.newPassword !== formData.confirmPassword) {
      setError('Passwords do not match');
      setLoading(false);
      return;
    }

    if (formData.newPassword.length < 6) {
      setError('Password must be at least 6 characters long');
      setLoading(false);
      return;
    }

    const result = await resetPassword({
      email: formData.email,
      otp: formData.otp,
      newPassword: formData.newPassword
    });
    
    if (result.success) {
      setSuccess('Password reset successfully! Redirecting to login...');
      setTimeout(() => {
        navigate('/login');
      }, 2000);
    } else {
      setError(result.error);
    }
    
    setLoading(false);
  };

  const handleOtpChange = (e) => {
    const value = e.target.value.replace(/\D/g, ''); // Only digits
    if (value.length <= 6) {
      setFormData({ ...formData, otp: value });
    }
  };

  const handleBackToEmail = () => {
    setStep(1);
    setError('');
    setSuccess('');
  };

  return (
    <div className="auth-container">
      <div className="auth-card">
        {step === 1 ? (
          <>
            <div className="auth-header">
              <h1>Forgot Password</h1>
              <p>Enter your email address and we'll send you an OTP to reset your password</p>
            </div>

            {error && <div className="auth-error">{error}</div>}
            {success && <div className="auth-success">{success}</div>}

            <form onSubmit={handleEmailSubmit} className="auth-form">
              <div className="form-group">
                <label htmlFor="email">Email Address</label>
                <input
                  type="email"
                  id="email"
                  name="email"
                  value={formData.email}
                  onChange={handleChange}
                  required
                  placeholder="Enter your email"
                />
              </div>

              <button 
                type="submit" 
                className="auth-button primary"
                disabled={loading}
              >
                {loading ? 'Sending OTP...' : 'Send Reset OTP'}
              </button>
            </form>

            <div className="auth-links">
              <p>
                Remember your password? <Link to="/login">Sign in</Link>
              </p>
            </div>
          </>
        ) : (
          <>
            <div className="auth-header">
              <h1>Reset Password</h1>
              <p>Enter the OTP sent to</p>
              <strong>{formData.email}</strong>
            </div>

            {error && <div className="auth-error">{error}</div>}
            {success && <div className="auth-success">{success}</div>}

            <form onSubmit={handlePasswordReset} className="auth-form">
              <div className="form-group">
                <label htmlFor="otp">Enter 6-digit OTP</label>
                <input
                  type="text"
                  id="otp"
                  name="otp"
                  value={formData.otp}
                  onChange={handleOtpChange}
                  required
                  placeholder="000000"
                  className="otp-input"
                  maxLength="6"
                />
              </div>

              <div className="form-group">
                <label htmlFor="newPassword">New Password</label>
                <input
                  type="password"
                  id="newPassword"
                  name="newPassword"
                  value={formData.newPassword}
                  onChange={handleChange}
                  required
                  placeholder="Enter new password"
                  minLength="6"
                />
              </div>

              <div className="form-group">
                <label htmlFor="confirmPassword">Confirm Password</label>
                <input
                  type="password"
                  id="confirmPassword"
                  name="confirmPassword"
                  value={formData.confirmPassword}
                  onChange={handleChange}
                  required
                  placeholder="Confirm new password"
                  minLength="6"
                />
              </div>

              <button 
                type="submit" 
                className="auth-button primary"
                disabled={loading || formData.otp.length !== 6}
              >
                {loading ? 'Resetting Password...' : 'Reset Password'}
              </button>
            </form>

            <div className="auth-links">
              <p>
                Wrong email? <span 
                  onClick={handleBackToEmail}
                  className="link-button"
                >
                  Go back
                </span>
              </p>
            </div>
          </>
        )}
      </div>
    </div>
  );
};

export default ForgotPassword; 