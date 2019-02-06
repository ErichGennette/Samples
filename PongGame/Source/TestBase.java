package a11rallypong;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestBase {
	paddle p;
	ball b;
	Boolean collided, expected;

	//Test#1 places the ball in a scenario where it would hit the paddle directly in the middle.
	@Test
	public void testCollisionCheck_middle() {
		p = new paddle(50, 50, 50, 50);
		b = new ball(50, 50, 50, 50);
		
		b.x = 50;
		p.x = 50;
		b.y = 100;
		p.y = 100;
		
		collided = b.collisionCheck(p);
		expected = true;
		
		assertEquals(expected, collided);
	}
	
	//Test#2 places the ball in a scenario where it would hit the lower part of the paddle.
	@Test
	public void testCollisionCheck_bottom() {
		p = new paddle(50, 50, 50, 50);
		b = new ball(50, 50, 50, 50);
		
		b.x = 50;
		p.x = 50;
		b.y = 150;
		p.y = 100;
		
		collided = b.collisionCheck(p);
		expected = true;
		
		assertEquals(expected, collided);
	}
	
	//Test#3 places the ball in a scenario where it would hit the upper part of the paddle.
	@Test
	public void testCollisionCheck_top() {
		p = new paddle(50, 50, 50, 50);
		b = new ball(50, 50, 50, 50);
		
		b.x = 50;
		p.x = 50;
		b.y = 50;
		p.y = 100;
		
		collided = b.collisionCheck(p);
		expected = true;
		
		assertEquals(expected, collided);
	}
	
	//Test#4 places the ball in a scenario where it would miss the paddle and fly below it.
	@Test
	public void testCollisionCheck_below() {
		p = new paddle(50, 50, 50, 50);
		b = new ball(50, 50, 50, 50);
		
		b.x = 50;
		p.x = 50;
		b.y = 200;
		p.y = 100;
		
		collided = b.collisionCheck(p);
		expected = false;
		
		assertEquals(expected, collided);
	}
	
	//Test#5 places the ball in a scenario where it would miss the paddle and fly over it.
	@Test
	public void testCollisionCheck_above() {
		p = new paddle(50, 50, 50, 50);
		b = new ball(50, 50, 50, 50);
		
		b.x = 50;
		p.x = 50;
		b.y = 0;
		p.y = 100;
		
		collided = b.collisionCheck(p);
		expected = false;
		
		assertEquals(expected, collided);
	}

}
