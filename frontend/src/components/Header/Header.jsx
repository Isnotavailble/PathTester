import styles from './Header.module.css';

export default function Header() {
  return (
    <header className={styles.header}>
      <div className={styles.logoContainer}>
        <h1 className={styles.title}>PATH<span className={styles.highlight}>TESTER</span></h1>
      </div>
    </header>
  );
}
