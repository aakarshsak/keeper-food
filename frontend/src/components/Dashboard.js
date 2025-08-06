import React from 'react';
import { useAuth } from '../context/AuthContext';
import FoodTracker from './FoodTracker';
import './Dashboard.css';

const Dashboard = () => {
  const { user, logout } = useAuth();

  const handleLogout = () => {
    logout();
  };

  return (
    <div className="dashboard">
      <header className="dashboard-header">
        <div className="header-content">
          <div className="user-info">
            <div className="user-avatar">
              {user?.profilePicture ? (
                <img src={user.profilePicture} alt="Profile" />
              ) : (
                <div className="avatar-placeholder">
                  {user?.firstName?.charAt(0) || 'U'}
                </div>
              )}
            </div>
            <div className="user-details">
              <h2>Welcome back, {user?.firstName || 'User'}!</h2>
              <p>{user?.email}</p>
              {!user?.emailVerified && (
                <span className="verification-badge">Email not verified</span>
              )}
            </div>
          </div>
          <button onClick={handleLogout} className="logout-btn">
            Logout
          </button>
        </div>
      </header>
      
      <main className="dashboard-main">
        <FoodTracker />
      </main>
    </div>
  );
};

export default Dashboard; 