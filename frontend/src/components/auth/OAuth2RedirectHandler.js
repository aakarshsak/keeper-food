import React, { useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';

const OAuth2RedirectHandler = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const { login } = useAuth();

  useEffect(() => {
    const handleOAuth2Redirect = async () => {
      const urlParams = new URLSearchParams(location.search);
      const token = urlParams.get('token');
      const error = urlParams.get('error');

      if (error) {
        const errorMessage = urlParams.get('message') || 'OAuth2 authentication failed';
        navigate('/login', { 
          state: { 
            error: errorMessage 
          }
        });
        return;
      }

      if (token) {
        // Store token and redirect to dashboard
        localStorage.setItem('token', token);
        
        try {
          // Get user info with the token
          const response = await fetch('http://localhost:8080/api/auth/me', {
            headers: {
              'Authorization': `Bearer ${token}`
            }
          });
          
          if (response.ok) {
            const userData = await response.json();
            // Manually trigger auth context update
            window.location.href = '/dashboard';
          } else {
            throw new Error('Failed to get user info');
          }
        } catch (error) {
          console.error('OAuth2 login error:', error);
          localStorage.removeItem('token');
          navigate('/login', { 
            state: { 
              error: 'Authentication failed. Please try again.' 
            }
          });
        }
      } else {
        navigate('/login', { 
          state: { 
            error: 'No authentication token received' 
          }
        });
      }
    };

    handleOAuth2Redirect();
  }, [location, navigate, login]);

  return (
    <div className="auth-container">
      <div className="auth-card">
        <div className="auth-header">
          <h1>Authenticating...</h1>
          <p>Please wait while we sign you in</p>
        </div>
        <div style={{ textAlign: 'center', padding: '20px' }}>
          <div className="spinner"></div>
        </div>
      </div>
    </div>
  );
};

export default OAuth2RedirectHandler; 