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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class SWTRegexTester {

	public static void main(String[] args) {
		final Display display = new Display();
		Shell shell = new Shell(display);
		GridLayout layout = new GridLayout(2, false);
		shell.setLayout(layout);

		final Label regexpLbl = new Label(shell, SWT.NONE);
		regexpLbl.setText("RegExp");

		final Text regexpText = new Text(shell, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		regexpText.setLayoutData(gd);

		final Label testLbl = new Label(shell, SWT.NONE);
		testLbl.setText("Test");
		final Text testText = new Text(shell, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		testText.setLayoutData(gd);

		final Label resultLbl = new Label(shell, SWT.NONE);
		resultLbl.setText("Result:");
		final Label result = new Label(shell, SWT.NONE);
		gd = new GridData(SWT.FILL, SWT.FILL, true, false);
		result.setLayoutData(gd);
		result.setText("none");
		Button b = new Button(shell, SWT.PUSH);
		b.setText("Test");
		b.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				testRegex(display, regexpText, testText, result);
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
					testRegex(display, regexpText, testText, result);
				}
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

	private static void testRegex(final Display display, final Text regexpText, final Text testText, final Label result) {
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
		result.setText(Boolean.toString(matcher.matches()));
		if (matcher.matches()) {
			result.setBackground(display.getSystemColor(SWT.COLOR_GREEN));
		} else {
			result.setBackground(display.getSystemColor(SWT.COLOR_RED));
		}
	}

}
