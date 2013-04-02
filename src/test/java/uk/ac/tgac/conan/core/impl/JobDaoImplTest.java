/**
 * RAMPART - Robust Automatic MultiPle AssembleR Toolkit
 * Copyright (C) 2013  Daniel Mapleson - TGAC
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 **/
package uk.ac.tgac.conan.core.impl;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations={"/applicationContext.xml"})
public class JobDaoImplTest {

	/*@Autowired
    ApplicationContext ctx;
	
	private JobDao jd;
	
	@Before
	public void before() {
		this.jd = ctx.getAutowireCapableBeanFactory().createBean(JobDaoImpl.class);
	}
	
	@Test
	@Transactional
	public void testGetAllJobs() {
		List<Job> jl = jd.getAllJobs();
		
		Job row0 = jl.get(0);
		Job row1 = jl.get(1);
		
		assertTrue(row0.getAuthor().equals("dan"));
		assertTrue(row1.getAuthor().equals("nizar"));
	}

	@Test
	@Transactional
	public void testGetJob() {
		Job j = jd.getJob(1L);
		
		assertTrue(j.getAuthor().equals("dan"));
	}
	
	@Test
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	@Rollback(true)
	public void testPersist() {
		
		Job j = new Job();
		j.setAuthor("test_author");
		j.setCollaborator("test_collaborator");
		j.setInstitution("test_institution");
		j.setTitle("test_title");
		j.setJiraSeqinfoId(500L);
		j.setMisoId(500L);
				
		long count = jd.count(); 
		jd.persist(j);
		long newCount = jd.count();
		
		assertTrue(newCount == count+1);
		
	}
	
	
	@Test
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	@Rollback(true)
	public void testPersistCascade() {
		
		Library l1 = new Library();
		l1.setName("test_lib");
		l1.setDataset(Dataset.RAW);
		l1.setAverageInsertSize(500);
		l1.setInsertErrorTolerance(0.3);
		l1.setReadLength(150);
		l1.setUsage("QT,ASM,SCF");
		l1.setIndex(1);
		
		List<Library> rawList = new ArrayList<Library>();
		rawList.add(l1);
		
		Library l2 = new Library();
		l2.setName("test_lib");
		l2.setDataset(Dataset.QT);
		l2.setAverageInsertSize(500);
		l2.setInsertErrorTolerance(0.3);
		l2.setReadLength(150);
		l2.setUsage("QT,ASM,SCF");
		l2.setIndex(1);
		
		List<Library> qtList = new ArrayList<Library>();
		qtList.add(l2);
		
		AssemblyStats m1 = new AssemblyStats();
		m1.setDataset(Dataset.RAW.name());
				
		List<AssemblyStats> msList = new ArrayList<AssemblyStats>();
		msList.add(m1);

        AssemblyStats i1 = new AssemblyStats();
		i1.setDesc("1");
				
		List<AssemblyStats> isList = new ArrayList<AssemblyStats>();
		isList.add(i1);
		
		RampartSettings rs = new RampartSettings();
		rs.setRampartVersion("0.2");
		rs.setQtTool("sickle");
		rs.setQtToolVersion("1.1");
		rs.setQtThreshold(0.3);
		rs.setQtMinLen(75);
		rs.setMassTool("abyss");
		rs.setMassToolVersion("1.3.4");
		rs.setMassKmin(41);
		rs.setMassKmax(95);
		rs.setImpScfTool("sspace");
		rs.setImpScfToolVersion("2.0-Basic");
		rs.setImpDegapTool("gapcloser");
		rs.setImpDegapToolVersion("1.12");
		rs.setImpClipMinLen(1000);
				
		
		Job j = new Job();
		j.setAuthor("test_author");
		j.setCollaborator("test_collaborator");
		j.setInstitution("test_institution");
		j.setTitle("test_title");
		j.setJiraSeqinfoId(500L);
		j.setMisoId(500L);
		j.setLibsRaw(rawList);
		j.setLibsQt(qtList);
		j.setMassStats(msList);
		j.setImproverStats(isList);
		j.setRampartSettings(rs);
		
				
		long count = jd.count(); 
		jd.persist(j);
		long newCount = jd.count();
		
		assertTrue(newCount == count+1);
		assertNotNull(j.getId());
	}  */
	
}
