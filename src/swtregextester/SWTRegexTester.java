package swtregextester;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class SWTRegexTester {

	public static void main(String[] args) {
		new SWTRegexTester().run();		
	}

	private Text regexpText;
	private Text testText;
	private Label result;
	private Text findText;
	private Text matchText;

	private void run() {

		final Display display = new Display();
		Shell shell = new Shell(display);
		GridLayout layout = new GridLayout(2, false);
		shell.setLayout(layout);

		final Label regexpLbl = new Label(shell, SWT.NONE);
		regexpLbl.setText("RegExp");

		regexpText = new Text(shell, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		regexpText.setLayoutData(gd);
		regexpText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.stateMask == SWT.CTRL && e.keyCode == 97) {
					regexpText.selectAll();
				}
				super.keyPressed(e);
			}
		});
		regexpText.addTraverseListener(new TraverseListener() {
			
			@Override
			public void keyTraversed(TraverseEvent e) {
				if (e.detail == SWT.TRAVERSE_TAB_PREVIOUS || e.detail == SWT.TRAVERSE_TAB_NEXT) {
					e.doit = true;
				}
			}
		});

		final Label testLbl = new Label(shell, SWT.NONE);
		testLbl.setText("Test");
		testText = new Text(shell, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		testText.setLayoutData(gd);
		testText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.stateMask == SWT.CTRL && e.keyCode == 97) {
					testText.selectAll();
				}
				super.keyPressed(e);
			}
		});
		testText.addTraverseListener(new TraverseListener() {
			
			@Override
			public void keyTraversed(TraverseEvent e) {
				if (e.detail == SWT.TRAVERSE_TAB_PREVIOUS || e.detail == SWT.TRAVERSE_TAB_NEXT) {
					e.doit = true;
				}
			}
		});

		final Label resultLbl = new Label(shell, SWT.NONE);
		resultLbl.setText("Result:");
		result = new Label(shell, SWT.NONE);
		gd = new GridData(SWT.FILL, SWT.FILL, true, false);
		result.setLayoutData(gd);
		result.setText("none");
		Button b = new Button(shell, SWT.PUSH);
		b.setText("Test");
		gd = new GridData(SWT.FILL, SWT.FILL, true, false);
		gd.horizontalSpan = 2;
		b.setLayoutData(gd);
		
		final Label matchLbl = new Label(shell, SWT.NONE);
		matchLbl.setText("Match");
		
		matchText = new Text(shell, SWT.BORDER);
		gd = new GridData(SWT.FILL, SWT.FILL, true, false);
		matchText.setLayoutData(gd);
		
		final Label groupLbl = new Label(shell, SWT.NONE);
		groupLbl.setText("Find 0");
		
		findText = new Text(shell, SWT.BORDER);
		gd = new GridData(SWT.FILL, SWT.FILL, true, false);
		findText.setLayoutData(gd);
		
		b.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				testRegex(display);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		// User can press return to validate
		KeyListener keyListener = new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.character == SWT.CR && e.stateMask == SWT.CTRL) {
					e.doit = false;
					testRegex(display);
				}
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				testRegex(display);
				super.keyReleased(e);
			}
		};
		
		regexpText.addKeyListener(keyListener);
		testText.addKeyListener(keyListener);
		

		shell.setMinimumSize(200, 200);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	private void testRegex(final Display display) {
		String pattern = regexpText.getText();
		Pattern compile = null;
		try {
			compile = Pattern.compile(pattern);
		} catch (PatternSyntaxException e1) {
			result.setText(e1.getMessage());
			return;
		}
		String text2 = testText.getText();
		Matcher matcher = compile.matcher(text2);
		boolean matches = matcher.matches();
		if (matches) {
			matchText.setText(matcher.group(0));
		} else {
			matchText.setText("");
		}
		
		matcher.reset();
		boolean find = matcher.find();
		result.setText(Boolean.toString(find));
		if (find) {
			findText.setText(matcher.group(0));
		} else {
			findText.setText("");
		}
		
		if (find || matches) {
			result.setBackground(display.getSystemColor(SWT.COLOR_GREEN));
		} else {
			result.setBackground(display.getSystemColor(SWT.COLOR_RED));
		}
	}

}
