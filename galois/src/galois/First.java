package galois;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Table;

public class First	{

	protected Shell shell;
	private Table table;
	public ReadCSV csv; 

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			First window = new First();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(509, 455);
		shell.setText("GSH builder");
		
		Button btnNewButton = new Button(shell, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					table.removeAll();
					FileDialog fd = new FileDialog(shell, SWT.OPEN);
			        fd.setText("Open");
			        fd.setFilterPath("C:/");
			        String[] filterExt = { "*.csv", "*.*" };
			        fd.setFilterExtensions(filterExt);
			        String selected = fd.open();
			        csv = new ReadCSV(selected);
			        csv.read();
			        csv.show(table);
				}
				catch(Exception ex) {
					
				}
			}
		});
		btnNewButton.setBounds(337, 348, 144, 50);
		btnNewButton.setText("Load");
		
		table = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION  | SWT.V_SCROLL
		        | SWT.H_SCROLL);
		table.setBounds(10, 10, 471, 304);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		Button btnNewButton_1 = new Button(shell, SWT.NONE);
		btnNewButton_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TopicThread T1 = new TopicThread(csv, "Thread-1");
				T1.start();

				DocThread T2 = new DocThread(csv, "Thread-2");;
				T2.start();
				
				
			}
		});
		btnNewButton_1.setBounds(170, 348, 138, 50);
		btnNewButton_1.setText("Extract");
		
		Button btnNewButton_2 = new Button(shell, SWT.NONE);
		btnNewButton_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				csv.deleteEqual();
			}
		});
		btnNewButton_2.setBounds(10, 348, 130, 50);
		btnNewButton_2.setText("Build");

	}
}
