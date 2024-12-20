import React, { useState } from 'react';
import './login.css';
import logo from '../../../assets/logo.png';
const Login = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');

  const handleLogin = (e) => {
    e.preventDefault();
    // Handle login logic here
    console.log('Email:', email);
    console.log('Password:', password);
  };
  const handleLogoClick = () => {
    window.location.href = "/";
  };
  return (
    <div>
      <div className='login-containerr'>
        <div className='loginImage'>
          <div className='loginText'>
            <img src={logo} alt='logo' onClick={handleLogoClick} id='logo'/>
            <h3>
              Best Carpool Website
            </h3>
          </div>
          </div>
          <div className="login-container">
      <h2>Hello Again</h2>
      <p>Welcome Back</p>
      <form onSubmit={handleLogin} className="login-form">
        <div className="input-group-login">
          <input
            type="email"
            placeholder='Email'
            id="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />
        </div>
        <div className="input-group-login">
          <input
            type="password"
            placeholder='password'
            id="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
        </div>
        <button type="submit">Login</button>
      </form>
      <p>
        Don't have an account?{' '}
        <a href="/signup" className="signup-link">Sign Up</a>
      </p>
    </div>
        </div>
    </div>
  );
};

export default Login;
